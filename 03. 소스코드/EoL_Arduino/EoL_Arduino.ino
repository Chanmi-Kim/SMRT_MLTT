#include <WiFi.h>
#include <mysql.h>
#include <SPI.h>
#include <sha1.h>
#include <DHT11.h>
#include <IRremote.h>

char ssid[] = "csp";              //와이파이 아이디 비밀번호
char pass[] = "1111133333";
IPAddress server_addr(119, 205, 68, 167);  // 디비 서버 주소

Connector my_conn;

IRsend irsend;
char user[] = "root";             //데이터베이스 아이디, 비밀번호
char password[] = "root";

int switch1 = 0;        //스위치1 상태 저장할변수
int sendIR = 0;         //IR명령어 확인할 변수
int DHTpin = 4;
#define POT_PIN A0      //조도 센서
int relay = 5; //릴레이에 5V 신호를 보낼 핀설정

DHT11 dht11(DHTpin);


bool wifiConn() {    // 와이파이 연결 함수

  if ( WiFi.status()) {
    return 1;
  }
  else {
    Serial.println("Connecting to Wi-Fi...");
    int status = WiFi.begin(ssid, pass);

    if ( status != WL_CONNECTED) {
      Serial.println("Couldn't get a WiFi connection.");
      return 0;
    }
    else {
      Serial.println("Connected to Wi-Fi.");
      IPAddress ip = WiFi.localIP();
      Serial.print("The IP address is: ");
      Serial.println(ip);
      return 1;
    }
  }
}

bool dbConn() {    // 데이터베이스 연결 함수
  if (my_conn.is_connected()) {
    return 1;
  }
  else {
    Serial.println("Connecting to database...");
    if (my_conn.mysql_connect(server_addr, 3306, user, password)) { //디비 연결
      return 1;
    }
    else {
      Serial.println("Connection to database failed.");
      return 0;
    }
  }

}

void updateStatus() {
  int err;
  float temp, humi;
  int temp2, humi2, lux;

  if ((err = dht11.read(humi, temp)) == 0)    //온습도 받아옴
  {
    /*
      Serial.print(F("temperature:"));
      Serial.print(temp);
      Serial.print(F(" humidity:"));
      Serial.print(humi);
      Serial.print(" lux : ");
    */
    lux = analogRead(POT_PIN);              //조도 받아옴
    Serial.print(lux);
    Serial.println();

    temp2 = (int)temp;
    humi2 = (int)humi;
  }
  /*
    else
    {
    Serial.println();
    Serial.print("Error No :");
    Serial.print(err);
    Serial.println();
    }
  */
  delay(DHT11_RETRY_DELAY); //delay for reread

  char update_s[75];  // 업데이트 쿼리문
  if (sendIR != 0) {
    sprintf(update_s, "UPDATE data.multitap set command = '0'");
  }
  else {
    sprintf(update_s, "UPDATE data.multitap set tem = '%d', hum = '%d', lux = '%d'", temp2, humi2, lux);
  }
  Serial.println(update_s);

  my_conn.cmd_query(update_s);    // perform query


}

void doQuery(const char *q) {     // do a query
  // based on 'pending_s'
  column_names *c;
  row_values *r;

  my_conn.cmd_query(q);

  c = my_conn.get_columns();

  int num_cols = c->num_fields;
  int rows = 0;

  r = my_conn.get_next_row();
  if (r) {
    rows++;
    for (int i = 0; i < num_cols; i++) {
      Serial.print(r->values[i]);
      if (i < num_cols - 1) {
        Serial.print(", ");
      }

      if (i == 2) {                     //세번째 인덱스일경우(switch 상태
        switch1 = atoi(r->values[i]);      // 스위치 온오프 상태
      }
      else if ( i == 3) {               //아이알 상태
        sendIR = atoi(r->values[i]);      //IR 연결상태
      }
    }

    my_conn.free_row_buffer();
  }


  Serial.print(rows);
  Serial.println(" rows in result.");

  my_conn.free_columns_buffer();

  if (rows == 0) {
    delay(500);
  }
  else {
    updateStatus();
    delay(1500);

  }
}

void setup() {

  Serial.begin(9600);
  pinMode (relay, OUTPUT); // relay를 output으로 설정한다.

  wifiConn(); // 와이파이 연결
  dbConn();   // 디비 연결

}

void loop() {
  char select_p[] = "SELECT * from data.multitap";
  if (wifiConn()) {    // 네트워크연결상태 확인
    if (dbConn()) {    // 데이터베이스 연결 및 연결상태 확인

      doQuery(select_p);    // then scan for raised flags
      Serial.println("switch1: ");
      Serial.println(switch1);

      if ( switch1 == 0) {
        Serial.println("relay off");
        digitalWrite (relay, HIGH); // 릴레이 OFF
      }
      else {
        Serial.println("relay on");
        digitalWrite (relay, LOW); // 릴레이 ON
      }

      if (sendIR != 0) {
        for (int i = 0; i < 3; i++) {
          if (sendIR == 001)            //LG TV on/off
            irsend.sendNEC(0x20DF10EF, 12);
          else if ( sendIR == 011)      //samsung TV on/off
            irsend.sendNEC(0xE0E040BF, 12);
          else if ( sendIR == 021)      //Sony Tv on/off
            irsend.sendSony(0xA90, 12);
          else if ( sendIR == 101)      //LG 에어컨 on/off
            irsend.sendNEC(0x616A817E, 12);
          else if ( sendIR == 102)      //LG 에어컨 온도 up
            irsend.sendNEC(0x616A6996, 12);
          else if ( sendIR == 103)      //LG 에어컨 온도 down
            irsend.sendNEC(0x616AE916, 12);
          else if ( sendIR == 111)      //samsung 에어컨 on/off
            irsend.sendNEC(0xA4B2955B, 12);
          else if ( sendIR == 112)      //samsung 에어컨 온도 up
            irsend.sendNEC(0x8166A15E, 12);
          else if ( sendIR == 113)      //samsung 에어컨 온도 down
            irsend.sendNEC(0x816651AE, 12);
          else if ( sendIR == 201)      //LG 프로젝트 on/off
            irsend.sendNEC(0x20F0B54A, 12);
          else if ( sendIR == 211)      //Samsung 프로젝트 on/off
            irsend.sendNEC(0x9B530587, 12);
          else if ( sendIR == 221)      //Sony 프로젝트 on/off
            irsend.sendSony(0x542A, 12);
          else if ( sendIR == 331)      //Olleh 셋톱박스 on/off
            irsend.sendNEC(0x9CA800FF, 12);
          else if ( sendIR == 301)      //LG 셋톱박스 on/off
            irsend.sendNEC(0x5B0310EF, 12);
          else if ( sendIR == 341)      //SKT 셋톱박스 on/off
            irsend.sendNEC(0x1FE807F, 12);

          delay(40);
        }
      }
      my_conn.disconnect();  // 데이터베이스 연결종료
      delay(1500);
    }
  }
}

