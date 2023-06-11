-- SELECT 1;
INSERT
IGNORE INTO roles (id, name) VALUES (1, "ROLE_USER"), (2, "ROLE_ADMIN");
INSERT
IGNORE INTO users (id, username, email, password, is_enabled, creation_time) VALUES
  (1, "WorldScrape", "admin@worldscrape.com", "$2a$10$QYQs0DIa5LHl23zzuzIbOu5R/86H3PGW9l6mbC2GncTWEFsdDuTZi", true, CURRENT_TIMESTAMP);
INSERT
IGNORE INTO user_roles (user_id, role_id) VALUES (1, 2);
 INSERT
IGNORE INTO scraper_file_configurations (id, name, description, url_style, policy, configuration_url, created_by_id) VALUES
 (1, "Trip Advisor Hotel", null, "https://www.tripadvisor.it/Hotel_%continue_url%", "PUBLIC", "https://s3.eu-central-1.amazonaws.com/world-scrape/1.yaml", 1),
 (2, "Trip Advisor Restaurant", null, "https://www.tripadvisor.com/Restaurant_%continue_url%", "PUBLIC", "https://s3.eu-central-1.amazonaws.com/world-scrape/2.yaml", 1),
  (3, "Booking.com Hotel", null, "https://www.booking.com/hotel/it/%continue_url%", "PUBLIC", "https://s3.eu-central-1.amazonaws.com/world-scrape/3.yaml", 1);
 INSERT
IGNORE INTO scraper_file_parameters (id, name, file_param_type, default_value, file_configuration_id) VALUES
   (1, "reviews_count", "number", "10", 1),
   (2, "images_count", "number", "10", 1),
   (3, "upload_images", "bool", "false", 1),
   (4, "reviews_count", "number", "10", 2),
   (5, "images_count", "number", "10", 2),
   (6, "upload_images", "bool", "false", 2),
   (7, "reviews_count", "number", "10", 3),
   (8, "images_count", "number", "10", 3),
   (9, "upload_images", "bool", "false", 3);

