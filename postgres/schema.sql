 \connect ridescheduler;
 DROP SCHEMA IF EXISTS ridescheduler;
 CREATE SCHEMA ridescheduler;
 DROP TABLE IF EXISTS ridescheduler.bus_stop;
 CREATE TABLE ridescheduler.bus_stop
 (
     id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     name     VARCHAR(255)                            NOT NULL,
     has_wifi BOOLEAN,
     CONSTRAINT pk_busstop PRIMARY KEY (id)
 );


 DROP TABLE IF EXISTS ridescheduler.bus_line;
 CREATE TABLE ridescheduler.bus_line
 (
     id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     name VARCHAR(255)                            NOT NULL,
     CONSTRAINT pk_busline PRIMARY KEY (id)
 );

 DROP TABLE IF EXISTS ridescheduler.bus_stop_in_bus_line;
 CREATE TABLE ridescheduler.bus_stop_in_bus_line
 (
     id                BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     bus_stop_id       BIGINT                                  NOT NULL,
     bus_line_id       BIGINT                                  NOT NULL,
     time_to_next_stop INT                                     NOT NULL,
     CONSTRAINT pk_busstopinbusline PRIMARY KEY (id),
     CONSTRAINT FK_BUSSTOPINBUSLINE_ON_BUS_LINE FOREIGN KEY (bus_line_id) REFERENCES ridescheduler.bus_line (id),
     CONSTRAINT FK_BUSSTOPINBUSLINE_ON_BUS_STOP FOREIGN KEY (bus_stop_id) REFERENCES ridescheduler.bus_stop (id)
 );

 DROP TABLE IF EXISTS ridescheduler.schedule;
 CREATE TABLE ridescheduler.schedule
 (
     id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     busline_id    BIGINT                                  NOT NULL,
     departuretime TIME                               NOT NULL,
     busstop_id    BIGINT                                  NOT NULL,
     CONSTRAINT pk_schedule PRIMARY KEY (id),
     CONSTRAINT FK_SCHEDULE_ON_BUSLINE FOREIGN KEY (busline_id) REFERENCES ridescheduler.bus_line (id),
     CONSTRAINT FK_SCHEDULE_ON_BUSSTOP FOREIGN KEY (busstop_id) REFERENCES ridescheduler.bus_stop (id)
 );
