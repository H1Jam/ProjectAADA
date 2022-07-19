//#include <stdlib.h>
//#include <stdint.h>
//#include <string.h>
//
//    byte dataHeader[3] = {65,67,69};//╟╔░ACE
//    byte dataIn[3] = {0,0,0};
////    byte buf[150];
//    /int bufIndex=0;
//    int dataStep = 0;
//    int dataLength = 0;
//
//    void parseIt(byte inp){
//      switch(dataStep){
//        case 0:
//        dataIn[0] = dataIn[1];
//        dataIn[1] = dataIn[2];
//        dataIn[2] = inp;
//        if (memcmp(dataHeader, dataIn, sizeof(dataIn))==0){
//          Serial.println("Got the header!");
//          memset(dataIn, 0, sizeof(dataIn));
//          bufIndex=0;
//          memset(buf, 0, sizeof(buf));
//          Serial.write(dataIn,3);
//          dataStep = 1;
//        }
//        break;
//        case 1:
//         dataLength = inp;
//         dataStep = (dataLength > 0)?2:0;
//         Serial.print("Got the Length:");
//         Serial.println(dataLength);
//        break;
//        case 2:
//          buf[bufIndex]=inp & 0xFF;
//          bufIndex++;// Todo: if bufIndex > sizeof(buf)
//          dataLength--;
//          if (dataLength<1){
//            dataStep = 0;
//            Serial.println("Data:");
//            for(int i = 0; i < bufIndex; i++){
//              Serial.print(i);
//              Serial.print(":");
//              Serial.println(buf[i]);
//            }
//          }
//        break;
//      }
//    }
