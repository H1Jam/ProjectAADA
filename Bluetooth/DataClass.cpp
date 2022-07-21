#include <stdint.h>
#include <string.h>
#include <string>
#include "DataProtocol.h"

class Button {
  private:
    void (*buttonCallback)(void);
  public:
    Button() {
      buttonCallback = NULL;
    }
    Button(void (*_buttonCallback)(void)) {
      buttonCallback = _buttonCallback;
    }
    void clicked() {
      if (*buttonCallback != NULL) {
        buttonCallback();
      }
    }
};

class DialKnob {
  private:
    int16_t * value = NULL;
  public:
    DialKnob() {
      DialKnob(NULL);
    }
    DialKnob(int16_t *val) {
      value = val;
    }
    void changed(int16_t val) {
      if (value != NULL) {
        *value = val;
      }
    }
};


class ScreenObjects {
  private:
    const int8_t maxButtons = 8;
    Button* buttons;
    DialKnob* dialKnobs;
    uint8_t buttonIndex = 0;
    uint8_t dialKnobIndex = 0;
  public:
    ScreenObjects() {
      buttons = new Button[maxButtons];
      dialKnobs = new DialKnob[maxButtons];
      dialKnobIndex = 0;
      buttonIndex = 0;
    }

    int32_t addButton(void (*buttonCallback)(void)) {
      if (buttonIndex < maxButtons) {
        buttons[buttonIndex] = Button(buttonCallback);
        buttonIndex++;
      }
      return (int32_t)buttonCallback;
    }

    void addDialKnob(int16_t * val) {
      if (dialKnobIndex < maxButtons) {
        dialKnobs[dialKnobIndex] = DialKnob(val);
        dialKnobIndex++;
      }
    }

    void knobChanged(int8_t tag, int val) {
      if (tag < dialKnobIndex) {
        dialKnobs[tag].changed(val);
      }
    }

    void clickButton(uint8_t tag) {
      if (tag < buttonIndex) {
        buttons[tag].clicked();
      }
    }
};


class Data1 {
  public:
    int64_t  mLng;
    float  mFlt;
    int16_t  mInt;
} __attribute__((__packed__));

class S2 {
  public:
    uint64_t  mLng;
    float  mFlt;
    int16_t  mInt;
} __attribute__((__packed__));



class ScreenTextLabel {
  private:
    uint8_t objID = ScreenIDs::label;
  public:
    int16_t x;
    int16_t y;
    int16_t tag;
    int16_t fontSize;
    int32_t color;
    std::string text;
    int getBytes(uint8_t *out) {
      //Todo: add a better lenght limit!
      memcpy(out, &objID, 1);
      //      x = __builtin_bswap16(x);
      //      y = __builtin_bswap16(y);
      //      tag = __builtin_bswap16(tag);
      //      fontSize = __builtin_bswap16(fontSize);
      //      color = __builtin_bswap32(color);
      memcpy(out + 1, &x, 2);
      memcpy(out + 3, &y, 2);
      memcpy(out + 5, &tag, 2);
      memcpy(out + 7, &fontSize, 2);
      memcpy(out + 9, &color, 4);
      if (text.length() > 32) {
        memcpy(out + 13, text.data(), 32);
        return 45;
      } else {
        memcpy(out + 13, text.data(), text.length());
        return 13 + text.length();
      }
    }
} __attribute__((__packed__));


class ScreenButton {
  private:
    uint8_t objID = ScreenIDs::button;
    int offset = 0;
  public:
    int16_t x;
    int16_t y;
    int16_t tag;
    int16_t fontSize;
    int32_t color;
    int32_t backColor;
    std::string text;
    int getBytes(uint8_t *out) {
      //Todo: add a better lenght limit!
      offset = 0;
      memcpy(out, &objID, 1);
      offset += sizeof(objID);
      memcpy(out + offset, &x, 2);
      offset += sizeof(x);
      memcpy(out + offset, &y, 2);
      offset += sizeof(y);
      memcpy(out + offset, &tag, 2);
      offset += sizeof(tag);
      memcpy(out + offset, &fontSize, 2);
      offset += sizeof(fontSize);
      memcpy(out + offset, &color, 4);
      offset += sizeof(color);
      memcpy(out + offset, &backColor, 4);
      offset += sizeof(backColor);
      if (text.length() > 32) {
        memcpy(out + offset, text.data(), 32);
        return (offset + 32);
      } else {
        memcpy(out + offset, text.data(), text.length());
        return offset + text.length();
      }
    }
};

class ScreenKnob {
  private:
    uint8_t objID = ScreenIDs::knob;
    int offset = 0;
  public:
    int16_t x;
    int16_t y;
    int16_t tag;
    int16_t dimSize;
    int16_t minValue;
    int16_t maxValue;
    int16_t startValue;
    std::string labelText;
    int getBytes(uint8_t *out) {
      //Todo: add a better lenght limit!
      offset = 0;
      memcpy(out, &objID, 1);
      offset += sizeof(objID);
      memcpy(out + offset, &x, 2);
      offset += sizeof(x);
      memcpy(out + offset, &y, 2);
      offset += sizeof(y);
      memcpy(out + offset, &tag, 2);
      offset += sizeof(tag);
      memcpy(out + offset, &dimSize, 2);
      offset += sizeof(dimSize);
      memcpy(out + offset, &minValue, 2);
      offset += sizeof(minValue);
      memcpy(out + offset, &maxValue, 2);
      offset += sizeof(maxValue);
      memcpy(out + offset, &startValue, 2);
      offset += sizeof(startValue);
      if (labelText.length() > 32) {
        memcpy(out + offset, labelText.data(), 32);
        return (offset + 32);
      } else {
        memcpy(out + offset, labelText.data(), labelText.length());
        return offset + labelText.length();
      }
    }
};

class ScreenGauge {
  private:
    uint8_t objID = ScreenIDs::gauge1;
    int offset = 0;
  public:
    int16_t x;
    int16_t y;
    int16_t tag;
    int16_t dimSize = 100;
    float value = 0;
    float maxValue = 100.0f;
    uint8_t drawArc = 0;
    float arcGreenMaxVal = 0;
    float arcYellowMaxVal = 0;
    float arcRedMaxVal = 0;
    std::string unitTextLabel;
    int getBytes(uint8_t *out) {
      //Todo: add a better lenght limit!
      offset = 0;
      memcpy(out, &objID, 1);
      offset += sizeof(objID);
      memcpy(out + offset, &x, sizeof(x));
      offset += sizeof(x);
      memcpy(out + offset, &y, sizeof(y));
      offset += sizeof(y);
      memcpy(out + offset, &tag, sizeof(tag));
      offset += sizeof(tag);
      memcpy(out + offset, &dimSize, sizeof(dimSize));
      offset += sizeof(dimSize);
      memcpy(out + offset, &value, sizeof(value));
      offset += sizeof(value);
      memcpy(out + offset, &maxValue, sizeof(maxValue));
      offset += sizeof(maxValue);
      memcpy(out + offset, &drawArc, sizeof(drawArc));
      offset += sizeof(drawArc);
      memcpy(out + offset, &arcGreenMaxVal, sizeof(arcGreenMaxVal));
      offset += sizeof(arcGreenMaxVal);
      memcpy(out + offset, &arcYellowMaxVal, sizeof(arcYellowMaxVal));
      offset += sizeof(arcYellowMaxVal);
      memcpy(out + offset, &arcRedMaxVal, sizeof(arcRedMaxVal));
      offset += sizeof(arcRedMaxVal);
      if (unitTextLabel.length() > 32) {
        unitTextLabel.substr(0, 32);
      }
      memcpy(out + offset, unitTextLabel.data(), unitTextLabel.length());
      return offset + unitTextLabel.length();
    }
};


class ScreenMap {
  private:
    uint8_t objID = ScreenIDs::mapView;
    int offset = 0;
  public:
    int16_t x;
    int16_t y;
    int16_t tag;
    int16_t width = 100;
    int16_t height = 100;
    float lat = 0.0f;
    float lon = 0.0f;
    float mapOrientation = 0.0f;
    uint8_t zoom = 1;
    int getBytes(uint8_t *out) {
      //Todo: add a better lenght limit!
      tag = 1; // Only one map for now! will extend it.
      offset = 0;
      memcpy(out, &objID, 1);
      offset += sizeof(objID);
      memcpy(out + offset, &x, sizeof(x));
      offset += sizeof(x);
      memcpy(out + offset, &y, sizeof(y));
      offset += sizeof(y);
      memcpy(out + offset, &tag, sizeof(tag));
      offset += sizeof(tag);
      memcpy(out + offset, &width, sizeof(width));
      offset += sizeof(width);
      memcpy(out + offset, &height, sizeof(height));
      offset += sizeof(height);
      memcpy(out + offset, &lat, sizeof(lat));
      offset += sizeof(lat);
      memcpy(out + offset, &lon, sizeof(lon));
      offset += sizeof(lon);
      memcpy(out + offset, &mapOrientation, sizeof(mapOrientation));
      offset += sizeof(mapOrientation);
      memcpy(out + offset, &zoom, sizeof(zoom));
      offset += sizeof(zoom);
      return offset;
    }
};

class ScreenMapMarker {
  private:
    uint8_t objID = ScreenIDs::mapMarker;
    int offset = 0;
  public:
    int16_t tag;
    float lat = 0.0f;
    float lon = 0.0f;
    float rotation = 0.0f;
    uint8_t iconId = 0;
    uint8_t cmdId = 0;
    int getBytes(uint8_t *out) {
      //Todo: add a better lenght limit!
      offset = 0;
      memcpy(out, &objID, 1);
      offset += sizeof(objID);
      memcpy(out + offset, &tag, sizeof(tag));
      offset += sizeof(tag);
      memcpy(out + offset, &lat, sizeof(lat));
      offset += sizeof(lat);
      memcpy(out + offset, &lon, sizeof(lon));
      offset += sizeof(lon);
      memcpy(out + offset, &rotation, sizeof(rotation));
      offset += sizeof(rotation);
      memcpy(out + offset, &iconId, sizeof(iconId));
      offset += sizeof(iconId);
      memcpy(out + offset, &cmdId, sizeof(cmdId));
      offset += sizeof(cmdId);
      return offset;
    }
};

class ScreenSwitch {
  private:
    uint8_t objID = ScreenIDs::toggleSwitch;
    int offset = 0;
  public:
    int16_t x;
    int16_t y;
    int16_t tag;
    uint8_t cmdId = 0;
    bool switchValue;
    int16_t fontSize;
    int16_t textColor;
    std::string labelText;
    int getBytes(uint8_t *out) {
      //Todo: add a better lenght limit!
      offset = 0;
      memcpy(out, &objID, 1);
      offset += sizeof(objID);
      memcpy(out + offset, &x, 2);
      offset += sizeof(x);
      memcpy(out + offset, &y, 2);
      offset += sizeof(y);
      memcpy(out + offset, &tag, 2);
      offset += sizeof(tag);
      memcpy(out + offset, &cmdId, sizeof(cmdId));
      offset += sizeof(cmdId);
      memcpy(out + offset, &switchValue, sizeof(switchValue));
      offset += sizeof(switchValue);
      memcpy(out + offset, &fontSize, sizeof(fontSize));
      offset += sizeof(fontSize);
      memcpy(out + offset, &textColor, sizeof(textColor));
      offset += sizeof(textColor);
      if (labelText.length() > 32) {
        labelText.substr(0, 32);
      }
      memcpy(out + offset, labelText.data(), labelText.length());
      return offset + labelText.length();
    }
};
