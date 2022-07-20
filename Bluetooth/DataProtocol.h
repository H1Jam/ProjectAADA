#ifndef __DATAPROTOCOL_H
#define __DATAPROTOCOL_H
#include <stdint.h>
#include <string.h>
#define MAX_DATA_LENGHT  100

static  int BLACK       = 0xFF000000;
static  int DKGRAY      = 0xFF444444;
static  int GRAY        = 0xFF888888;
static  int LTGRAY      = 0xFFCCCCCC;
static  int WHITE       = 0xFFFFFFFF;
static  int RED         = 0xFFFF0000;
static  int GREEN       = 0xFF00FF00;
static  int BLUE        = 0xFF0000FF;
static  int YELLOW      = 0xFFFFFF00;
static  int CYAN        = 0xFF00FFFF;
static  int MAGENTA     = 0xFFFF00FF;
static  int TRANSPARENT = 0;

uint8_t const dataHeader[3] = {199, 201, 176};
enum DataDirection {toAndroid, fromAndroid};
enum ScreenIDs : uint8_t {
  button = 6,
  label,
  gauge1,
  gauge2,
  knob,
  mapView,
  mapMarker
  toggleSwitch
};

int sendFrame(uint8_t *destBuffer, uint8_t const *buffer, size_t len);
bool checkCRC(uint8_t const *buffer, size_t len);
class DataParser {
  private:
    uint8_t dataIn[3] = {0, 0, 0};
    uint8_t buf[MAX_DATA_LENGHT];
    int bufIndex = 0;
    int dataStep = 0;
    int dataLength = 0;
  public:
    int parseIt(uint8_t inp, uint8_t *out);
};

#endif /* __DATAPROTOCOL_H */
