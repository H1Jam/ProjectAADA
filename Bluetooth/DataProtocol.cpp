#include "DataProtocol.h"
#include "crc16.h"
#include <Arduino.h> // Just for Serial.print! Remove it!!!
uint16_t crcCalc;
uint16_t crcRcv;
unsigned long mLastDigitTimeStamp = 0;
void printArray(uint8_t const *buffer, size_t len);

int DataParser::parseIt(uint8_t inp, uint8_t *out) {
  switch (dataStep) {
    case 0:
      dataIn[0] = dataIn[1];
      dataIn[1] = dataIn[2];
      dataIn[2] = inp;
      if (memcmp(dataHeader, dataIn, sizeof(dataIn)) == 0) { // we got the header!
        memset(dataIn, 0, sizeof(dataIn));// Clear it for the next time.
        bufIndex = 0; // Reset the
        memset(buf, 0, sizeof(buf));
        dataStep = 1;
      }
      break;
    case 1:
      if (inp > MAX_DATA_LENGHT) {
        dataStep = 0;
        return 0;
      }
      dataLength = inp;
      dataStep = (dataLength > 0) ? 2 : 0;
      break;
    case 2:
      buf[bufIndex] = inp;
      bufIndex++;
      if (bufIndex > MAX_DATA_LENGHT) { // Some extra precaution.
        dataStep = 0;
        return 0;
      }
      if (bufIndex == dataLength) {
        dataStep = 0;
        if (checkCRC(buf, bufIndex)) {
          memcpy(out, buf, bufIndex);
          return bufIndex;
        }
      }
      break;
  }
  return 0;
}
bool isTimedOut() {
  if ((millis() - mLastDigitTimeStamp) > 100) {
    mLastDigitTimeStamp = millis();
    return true;
  }
  mLastDigitTimeStamp = millis();
  return false;
}

//Header[3]+Len[1]+Payload[Len]+CRC16[2]
bool checkCRC(uint8_t const *buffer, size_t len) {
  printArray(buffer, len);
  crcCalc = crc16(buffer + 1, len - 3);
  memcpy(&crcRcv, buffer + len - 2, 2);
  if (crcCalc == crcRcv) {
    return true;
  } else {
    Serial.print("\nBad CRC! CALC =") ;
    Serial.print(crcCalc) ;
    Serial.print(", Rcv =") ;
    Serial.println(crcRcv) ;
    // printArray(buffer, len);
    return false;
  }
}

uint16_t crcTmp = 0;
int sendFrame(uint8_t *destBuffer, uint8_t const *buffer, size_t len) {
  if (len < 1) return 0;
  if (len > MAX_DATA_LENGHT) return 0;
  memcpy(destBuffer, dataHeader, 3);
  destBuffer[3] = (len + 3) & 0xFF;
  destBuffer[4] = toAndroid & 0xFF;
  memcpy(destBuffer + 5, buffer, len);
  crcTmp = crc16(buffer, len);
  destBuffer[5 + len] = crcTmp & 0xFF;
  destBuffer[5 + len + 1] = (crcTmp >> 8) & 0xFF;
  Serial.print("\n------------\n[");
  for (int i = 0; i < (len + 7); i++) {
    Serial.print(destBuffer[i]);
    Serial.print(", ");
  }
  Serial.print("]\n------------\n");
  return len + 7;
}

void printArray(uint8_t const *buffer, size_t len) {
  Serial.print("\n------------\n[");
  for (int i = 0; i < (len); i++) {
    Serial.print(buffer[i]);
    Serial.print(", ");
  }
  Serial.print("]\n------------\n");
}
