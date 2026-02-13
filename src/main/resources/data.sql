-- Seed default rates for all transportation types
INSERT INTO rates (transportation_type, base_rate_per_unit, volume_multiplier, size_multiplier, effective_from, effective_to, created_at)
VALUES ('BOAT', 10.0000, 1.5000, 2.0000, '2025-01-01 00:00:00', NULL, '2025-01-01 00:00:00');

INSERT INTO rates (transportation_type, base_rate_per_unit, volume_multiplier, size_multiplier, effective_from, effective_to, created_at)
VALUES ('TRUCK', 12.0000, 1.2000, 1.8000, '2025-01-01 00:00:00', NULL, '2025-01-01 00:00:00');

INSERT INTO rates (transportation_type, base_rate_per_unit, volume_multiplier, size_multiplier, effective_from, effective_to, created_at)
VALUES ('RAIL', 8.0000, 1.1000, 1.5000, '2025-01-01 00:00:00', NULL, '2025-01-01 00:00:00');
