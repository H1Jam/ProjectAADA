#include "BluetoothSerial.h"
#include "DataProtocol.h"
#include "DataClass.cpp"
#include "crc16.h"
#include <string>
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif
// make an echo protocole.

BluetoothSerial SerialBT;
bool doWait = false;
byte inp = 0;
const int ledPin = 2;
byte buf[100];
byte bufTest[64];
byte bufHeader[] = {199, 201, 176};
byte bufFrame[100];
DataParser dataParser;
Data1 data1;
Data1 data2;
uint8_t rbuf[] = { 49, 50, 51, 52, 53, 54, 55, 56, 57}; // 123456789
uint8_t rbufTextLabel[] = {17, 0, 50, 0, 52, 0, 20, 0, 28, 255, 0, 255, 255, 66, 67, 68, 69, 70}; // 123456789 BCDEF
ScreenTextLabel textLabel;
ScreenButton screenButton;
ScreenKnob screenknob;
ScreenGauge screenGauge;
ScreenMap screenMap;
ScreenMapMarker screenMapMarker1;
ScreenMapMarker screenMapMarker2;
bool label_counter = false;
bool button_counter = false;
int inpTemp;
uint8_t frameSendBuffer[MAX_DATA_LENGHT + 6];
int frameSendBufferSize = 0;

int16_t knb1;
int16_t knb2;
int16_t knb3;
int16_t knb4;

void my_int_func(int x)
{
  Serial.print("\nHello From a Function Pointer! ");
  Serial.println( x );
}

void my_int_func_(int x)
{
  Serial.print("\nHello From_a_Function Pointer! ^_^");
  Serial.println( x );
}


void my_int_func2()
{
  Serial.print("\nHello From a Function Pointer! no Input\n");
}

int my_int_func3(int x)
{
  Serial.print("\nHello From a Cat! ");
  Serial.println( x );
  return x * 2;
}

void my_func(void (*func)(int), int ddata) {
  func(ddata);
}
int datacntr = 0;
void buttonClicked0()
{
  datacntr++;
  Serial.print("\nA 0 button has been clicked!\n");
}

void buttonClicked1()
{
  screenButton.x = 60;
  screenButton.y = 220;
  screenButton.tag = 3;
  screenButton.color = BLACK;
  button_counter = !button_counter;
  if (button_counter) {
    screenButton.text = "@C++@";
    screenButton.backColor = MAGENTA;
    screenButton.fontSize = 23;
  } else {
    screenButton.text = "!C++! ";
    screenButton.backColor = YELLOW;
    screenButton.fontSize = 18;
  }
  int dLenght = screenButton.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }
  datacntr++;
  Serial.print("\nA 1 button has been clicked!\n");
}

ScreenObjects screenObjects;
void setup() {
  Serial.begin(115200);

  void (*foo[3])(int);
  foo[0] = &my_int_func;
  foo[1] = my_int_func_;
  foo[0](2);
  foo[1](9);

  void (*bar)(void);
  bar = &my_int_func2;
  bar();

  my_func(foo[0], 54);
  my_func(foo[1], 79);

  screenObjects.addDialKnob(&knb1);
  screenObjects.addDialKnob(&knb2);
  screenObjects.addDialKnob(&knb3);
  screenObjects.addDialKnob(&knb4);
  Serial.println( screenObjects.addButton(&buttonClicked0));
  Serial.println( screenObjects.addButton(&buttonClicked1));
  Serial.println(  screenObjects.addButton(&buttonClicked2));
  Serial.println(  screenObjects.addButton(&buttonClicked3));

  // screenObjects.clickButton(2);
  //screenObjects.clickButton(0);
  //screenObjects.clickButton(1);
  // screenObjects.clickButton(2);

  Serial.print("datacntr: ");
  Serial.println(datacntr);



  int (*cat)(int);
  cat = my_int_func3;
  int rt = cat(10);
  Serial.println(rt);

  Serial.print("\n Size of void (*foo)(int):");
  Serial.println(sizeof(foo));
  Serial.print("\n Size of void (*bar)(void):");
  Serial.println(sizeof(bar));
  Serial.print("\n Size of cat:");
  Serial.println(sizeof(cat));

  int crcC = 0;

  //sendFrame(rbuf, 9);
  crcC = crc16(rbuf, 9);// Must be 58828. Check https://crccalc.com/
  Serial.print("CRC must be 58828 : ");
  Serial.println(crcC);
  static_assert(sizeof(Data1) == 14, "this program requires working __attribute__((__packed__))");

  String devName = "ESP32testB";
  pinMode (ledPin, OUTPUT);

  SerialBT.begin(devName); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
  Serial.println("devName:");
  Serial.println(devName);
  //data1.mLng = 32664124384564L;
  data1.mLng = 654321L;
  data1.mFlt = 16.2132564f;
  data1.mInt = -1643;
  char* my_s_bytes = reinterpret_cast<char*>(&data1);
  bool aTst = true;
  screenObjects.registerSwitch(1, &aTst);
  screenObjects.updateSwitch(1, false);
  Serial.println(aTst);
  screenObjects.updateSwitch(1, true);
  Serial.println(aTst);
  screenObjects.updateSwitch(1, true);
  Serial.println(aTst);
  screenObjects.updateSwitch(1, false);
  Serial.println(aTst);
}


void loop() {
  // put your main code here, to run repeatedly:
  doWait = true;
  if (Serial.available()) {
    inpTemp = Serial.read();
    //parseIt(inpTemp);
    //int a = dataParser.parseIt(inpTemp ,buf);
    // if (a>0){
    //    Serial.print("Got:");
    //    Serial.println(a);
    //    Serial.println(*buf);
    //    Serial.println(*(buf+1));
    //    Serial.write(buf,a);
    //memcpy(&data2, buf, a);
    //Serial.println(data2.mLng);
    //Serial.println(data2.mFlt,5);
    //Serial.println(data2.mInt);
    //    }
    // digitalWrite (ledPin, 1);
    //SerialBT.write(Serial.read());
    // digitalWrite (ledPin, 0);
    doWait = false;
  } else {
    //    byte *bytes = byte[sizeof(Data1)];
    //    bytes = (byte) data1;
    //Serial.write((unsigned char*)(&data1), 10);
    // delay(1000);
  }
  if (SerialBT.available()) {
    inp = SerialBT.read();
    int a = dataParser.parseIt(inp , buf);
    if (a > 0) {
      Serial.print("Got:");
      Serial.println(a);
      Serial.println(*buf);
      Serial.println(*(buf + 1));
      Serial.write(buf, a);
      Serial.print("\n D:");
      if (buf[1] == ScreenIDs::button) {
        screenObjects.clickButton(buf[2]);
      }
      if (buf[1] == ScreenIDs::knob) {
        int16_t aaa = ((0xFFFF & buf[5]) << 8) | (buf[4] & 0xFF);
        screenObjects.knobChanged(buf[2], aaa);
        Serial.print("\naaa:");
        Serial.print(aaa);
        Serial.print("\nknb:");
        Serial.println(knb1);
        Serial.println(knb2);
        Serial.println(knb3);
        Serial.println(knb4);
      }

      for (int i = 0; i < a ; i++ ) {
        Serial.print(buf[i]);
        Serial.print(",");
      }
      // Todo: Handle the data!
      //      memcpy(&data2, buf, sizeof(data2));
      //      Serial.println("\n------------");
      //      Serial.println(data2.mLng);
      //      Serial.println(data2.mFlt, 5);
      //      Serial.println(data2.mInt);
      Serial.println("------------");
      //char* my_s_bytes = reinterpret_cast<char*>(&data1);
      //memcpy(&bufTest, &bufHeader, sizeof(bufHeader));
      //  memcpy(bufTest, &data1, sizeof(data1));

      //bufTest[3]=sizeof(data1);
      // memcpy(bufTest+4, &data1, sizeof(data1));
      // Serial.print("\n bufTest:");
      //for (int i = 0; i <15; i++ ){
      //  Serial.print((int)bufTest[i]);
      //  Serial.print(" ");
      //}
      Serial.print("\n");
      //SerialBT.write(bufTest, sizeof(bufHeader)+sizeof(data1)+1);
    }
    // SerialBT.write(inp);
    // Serial.println(inp);
    doWait = false;
  }
  if (doWait) {
    delay(1);
  }
}

void buttonClicked2()
{
  textLabel.x = 60;
  textLabel.y = 120;
  textLabel.tag = 21;
  //textLabel.text = "Hello From C++! ";
  label_counter = !label_counter;
  if (label_counter) {
    textLabel.text = "Second Label!";
    textLabel.color = BLUE;
    textLabel.fontSize = 23;
  } else {
    textLabel.text = "Hello From C++! ";
    textLabel.color = RED;
    textLabel.fontSize = 18;
  }

  //textLabel.text +=  (adds);
  //uint8_t* textLabel_bytes = reinterpret_cast<uint8_t*>(&textLabel);
  int dLenght = textLabel.getBytes(bufFrame);
  //memset(frameSendBuffer, 0,dLenght )
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }
  Serial.print("\nA 2 button has been clicked!\n");
}

bool hasMap = false;
bool hasMapMarker = false;

void buttonClicked3()
{
  if (hasMap) {
    if (!hasMapMarker) {
      addMapMarker();
      hasMapMarker = true;
    } else {
      removeMapMarker();
      hasMapMarker = false;
    }
  } else {
    addMap();
    hasMap = true;
  }
  addSwitch();
  Serial.print("\n3rd button has been clicked!\n");
}
ScreenSwitch screenSwitch;

void addSwitch() {

  screenSwitch.x = 50;
  screenSwitch.y = 270;
  screenSwitch.tag = 99;
  screenSwitch.cmdId = 0;
  screenSwitch.switchValue = hasMapMarker;
  screenSwitch.fontSize = 30;
  screenSwitch.textColor = MAGENTA;
  screenSwitch.labelText = "SwitchFromC++";
  int dLenght = screenSwitch.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }

}


float gVal = 0;
uint8_t iconId = 0;
void addMapMarker()
{
  gVal += 10;
  screenMapMarker1.tag = 1;
  screenMapMarker1.lat = 43.732825f;
  screenMapMarker1.lon = -79.442881f;
  screenMapMarker1.rotation = gVal;
  screenMapMarker1.iconId = iconId;
  screenMapMarker1.cmdId = 0;
  int dLenght = screenMapMarker1.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }
  iconId++;
  if (iconId > 11) {
    iconId = 0;
  }
  screenMapMarker2.tag = 2;
  screenMapMarker2.lat = 43.733825f;
  screenMapMarker2.lon = -79.443881f;
  screenMapMarker2.rotation = gVal;
  screenMapMarker2.iconId = iconId;
  screenMapMarker2.cmdId = 0;
  dLenght = screenMapMarker2.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }
  Serial.print("\naddMapMarker!\n");
}

void removeMapMarker()
{
  screenMapMarker2.tag = 1;
  screenMapMarker2.cmdId = 1;
  int dLenght = screenMapMarker2.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }
  Serial.print("\naddMapMarker!\n");
}

void addMap()
{
  gVal += 10;
  screenMap.x = 10;
  screenMap.y = 450;
  screenMap.tag = 2;
  screenMap.width = 300;
  screenMap.height = 200;
  screenMap.lat = 43.730825f;
  screenMap.lon = -79.440881f;
  screenMap.mapOrientation = gVal;
  screenMap.zoom = 16;
  int dLenght = screenMap.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }
  Serial.print("\naddMap!\n");
}

void addKnob()
{
  screenknob.x = 120;
  screenknob.y = 500;
  screenknob.tag = 2;
  screenknob.dimSize = 95;
  screenknob.minValue = -500;
  screenknob.maxValue = 500;
  screenknob.startValue = 200;
  screenknob.labelText = "Servo03";
  int dLenght = screenknob.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }

  Serial.print("\naddKnob!\n");
}


void addGauge()
{
  gVal += 10;
  screenGauge.x = 120;
  screenGauge.y = 500;
  screenGauge.dimSize = 120;
  screenGauge.value = gVal;
  screenGauge.tag = 11;
  screenGauge.maxValue = 200.0f;
  screenGauge.drawArc = 100;
  screenGauge.arcGreenMaxVal = 100.0f;
  screenGauge.arcYellowMaxVal = 150.0f;
  screenGauge.arcRedMaxVal = 180.0f;
  screenGauge.unitTextLabel = "Celcesius";
  int dLenght = screenGauge.getBytes(bufFrame);
  frameSendBufferSize = sendFrame(frameSendBuffer, bufFrame, dLenght);
  if (frameSendBufferSize > 0) {
    SerialBT.write(frameSendBuffer, frameSendBufferSize);
  }

  Serial.print("\naddaddGauge!\n");
}
