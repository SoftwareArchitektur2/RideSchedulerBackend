INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('stop1', true);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('stop2', false);
INSERT INTO ridescheduler.bus_stop (name, has_wifi) VALUES ('stop3', false);

INSERT INTO ridescheduler.bus_line (name) VALUES ('line1');

INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (0, 0, 2);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (1, 0, 2);
INSERT INTO ridescheduler.bus_stop_in_bus_line (bus_stop_id, bus_line_id, time_to_next_stop) VALUES (2, 0, 2);