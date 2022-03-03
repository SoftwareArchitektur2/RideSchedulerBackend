INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Geiststraße', true);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Kolde-Ring /LVM', false);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster DZ HYP /IHK', false);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Inselbogen / Sparkassenzentrum', true);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Buckstraße', false);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster P + R Weseler Str.', false);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Boeselagerstr.', true);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Kerkheideweg / Brillux.', false);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Mersmannsstiege', true);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Umspannwerk', false);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('Münster Dingbängerweg', false);

INSERT INTO ridescheduler.bus_line (name) VALUES ('StadtBus 15');
INSERT INTO ridescheduler.bus_line (name) VALUES ('StadtBus 16');

INSERT INTO ridescheduler.schedule (busline_id, departuretime, busstop_id) VALUES (0, '08:00:00', 10);
INSERT INTO ridescheduler.schedule (busline_id, departuretime, busstop_id) VALUES (0, '08:10:00', 0);
INSERT INTO ridescheduler.schedule (busline_id, departuretime, busstop_id) VALUES (1, '08:20:00', 1);
INSERT INTO ridescheduler.schedule (busline_id, departuretime, busstop_id) VALUES (1, '08:30:00', 2);

INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (0, 0, 2);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (1, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (2, 0, 2);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (3, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (4, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (5, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (6, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (7, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (8, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (9, 0, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (10, 0, 1);

INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (5, 1, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (6, 1, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (7, 1, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (8, 1, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (9, 1, 1);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (10, 1, 1);