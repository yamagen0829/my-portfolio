-- categories テーブル
INSERT IGNORE INTO categories (id, name) VALUES (1, '和食');
INSERT IGNORE INTO categories (id, name) VALUES (2, '洋食');
INSERT IGNORE INTO categories (id, name) VALUES (3, '中華');
INSERT IGNORE INTO categories (id, name) VALUES (4, '焼肉');
INSERT IGNORE INTO categories (id, name) VALUES (5, '鍋');
INSERT IGNORE INTO categories (id, name) VALUES (6, 'カレー');
INSERT IGNORE INTO categories (id, name) VALUES (7, 'ラーメン');
INSERT IGNORE INTO categories (id, name) VALUES (8, '居酒屋');

-- restaurants テーブル
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (1, 1, 'SAMURAI和食', 'restaurant01.jpg', '最寄り駅から徒歩10分。美味しい和食料理が揃ってます。', '17:00~24:00', 3000, '460-XXXX', '愛知県名古屋市西五条東X-XX-XX', '052-XXX-XXX', '日曜日', 30, '2023-04-02','2023-05-03');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (2, 2, 'SAMURAI洋食レストラン', 'restaurant02.jpg', '最寄り駅から徒歩5分。美味しい料理が揃ってます。', '11:00~24:00', 5000, '460-XXXX', '愛知県名古屋市西五条南X-XX-XX', '052-XXX-XXX', '日曜日', 40, '2023-04-01','2023-05-04');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (3, 3, 'SAMURAI中華', 'restaurant03.jpg', '最寄り駅から徒歩1分。美味しい中華料理が揃ってます。', '11:00~24:00', 1000, '460-XXXX', '愛知県名古屋市西五条西X-XX-XX', '052-XXX-XXX', '木曜日', 20, '2023-04-04','2023-05-06');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (4, 4, 'SAMURAI焼肉', 'restaurant04.jpg', '最寄り駅から徒歩3分。新鮮なお肉が揃ってます。', '11:00~24:00', 2000, '460-XXXX', '愛知県名古屋市西五条北X-XX-XX', '052-XXX-XXX', '水曜日', 30, '2023-04-05','2023-05-07');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (5, 5, 'SAMURAI鍋', 'restaurant05.jpg', '最寄り駅から徒歩6分。美味しい鍋料理が揃ってます。', '11:00~24:00', 2000, '460-XXXX', '愛知県名古屋市西五条東X-XX-XX', '052-XXX-XXX', '火曜日', 30, '2023-04-08','2023-05-09');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (6, 6, 'SAMURAIカレー', 'restaurant06.jpg', '最寄り駅から徒歩8分。インド風なカレー料理が揃ってます。', '11:00~24:00', 2000, '460-XXXX', '愛知県名古屋市西五条南X-XX-XX', '052-XXX-XXX', '水曜日', 20, '2023-04-01','2023-05-01');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (7, 7, 'SAMURAIラーメン', 'restaurant07.jpg', '最寄り駅から徒歩10分。美味しいラーメンが揃ってます。', '11:00~24:00', 1000, '460-XXXX', '愛知県名古屋市西五条西X-XX-XX', '052-XXX-XXX', '水曜日', 10, '2023-04-03','2023-05-02');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (8, 8, 'SAMURAI居酒屋', 'restaurant08.jpg', '最寄り駅から徒歩11分。珍しい酒が揃ってます。', '17:00~5:00', 3000, '460-XXXX', '愛知県名古屋市西五条北X-XX-XX', '052-XXX-XXX', '水曜日', 30, '2023-04-02','2023-05-05');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (9, 1, '和食レストラン', 'restaurant01.jpg', '最寄り駅から徒歩10分。美味しい和食料理が揃ってます。', '17:00~24:00', 3000, '460-XXXX', '愛知県名古屋市西五条東X-XX-XX', '052-XXX-XXX', '日曜日', 30, '2023-04-02','2023-05-03');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (10, 2, '洋食レストラン', 'restaurant02.jpg', '最寄り駅から徒歩5分。美味しい料理が揃ってます。', '11:00~24:00', 5000, '460-XXXX', '愛知県名古屋市西五条南X-XX-XX', '052-XXX-XXX', '日曜日', 40, '2023-04-01','2023-05-04');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (11, 3, '中華レストラン', 'restaurant03.jpg', '最寄り駅から徒歩1分。美味しい中華料理が揃ってます。', '11:00~24:00', 1000, '460-XXXX', '愛知県名古屋市西五条西X-XX-XX', '052-XXX-XXX', '木曜日', 20, '2023-04-04','2023-05-06');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (12, 4, '焼肉レストラン', 'restaurant04.jpg', '最寄り駅から徒歩3分。新鮮なお肉が揃ってます。', '11:00~24:00', 2000, '460-XXXX', '愛知県名古屋市西五条北X-XX-XX', '052-XXX-XXX', '水曜日', 30, '2023-04-05','2023-05-07');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (13, 5, '鍋レストラン', 'restaurant05.jpg', '最寄り駅から徒歩6分。美味しい鍋料理が揃ってます。', '11:00~24:00', 2000, '460-XXXX', '愛知県名古屋市西五条東X-XX-XX', '052-XXX-XXX', '火曜日', 30, '2023-04-08','2023-05-09');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (14, 6, 'カレーレストラン', 'restaurant06.jpg', '最寄り駅から徒歩8分。インド風なカレー料理が揃ってます。', '11:00~24:00', 2000, '460-XXXX', '愛知県名古屋市西五条南X-XX-XX', '052-XXX-XXX', '水曜日', 20, '2023-04-01','2023-05-01');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (15, 7, 'ラーメンレストラン', 'restaurant07.jpg', '最寄り駅から徒歩10分。美味しいラーメンが揃ってます。', '11:00~24:00', 1000, '460-XXXX', '愛知県名古屋市西五条西X-XX-XX', '052-XXX-XXX', '水曜日', 10, '2023-04-03','2023-05-02');
INSERT IGNORE INTO restaurants (id, category_id, name, image_name, description, opening_hours, price, postal_code, address, phone_number, regular_holiday, number_of_seat, created_at, updated_at) VALUES (16, 8, '居酒屋レストラン', 'restaurant08.jpg', '最寄り駅から徒歩11分。珍しい酒が揃ってます。', '17:00~5:00', 3000, '460-XXXX', '愛知県名古屋市西五条北X-XX-XX', '052-XXX-XXX', '水曜日', 30, '2023-04-02','2023-05-05');

-- roles テーブル
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- users テーブル
INSERT IGNORE INTO users (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled, paid) VALUES (1, '侍 太郎', 'サムライ タロウ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'taro.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true, true);
INSERT IGNORE INTO users (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled, paid) VALUES (2, '侍 花子', 'サムライ ハナコ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'hanako.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 2, true, true);
INSERT IGNORE INTO users (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled, paid) VALUES (3, '侍 義勝', 'サムライ ヨシカツ', '638-0644', '奈良県五條市西吉野町湯川X-XX-XX', '090-1234-5678', 'yoshikatsu.samurai@example.com', 'password', 1, false, false);
INSERT IGNORE INTO users (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled, paid) VALUES (4, '侍 幸美', 'サムライ サチミ', '342-0006', '埼玉県吉川市南広島X-XX-XX', '090-1234-5678', 'sachimi.samurai@example.com', 'password', 1, false, false);
INSERT IGNORE INTO users (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled, paid) VALUES (5, '侍 雅', 'サムライ ミヤビ', '527-0209', '滋賀県東近江市佐目町X-XX-XX', '090-1234-5678', 'miyabi.samurai@example.com', 'password', 1, false, false);

-- verification_tokens テーブル
INSERT IGNORE INTO verification_tokens (user_id, token, expiry_date) VALUES (1, 'example_token', DATE_ADD(NOW(), INTERVAL 1 DAY));

-- reviews テーブル
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (1, 5, 1, '美味しかった。', 5, '2023-08-03', '2023-08-04' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (2, 5, 2, 'うまい！', 4, '2023-08-04', '2023-08-05' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (3, 5, 3, '素晴らしかった。', 3, '2023-08-07', '2023-08-07' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (4, 5, 4, '接客も良かった。', 4, '2023-08-08', '2023-08-08' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (5, 5, 5, 'いまいち。', 1, '2023-08-03', '2023-08-04' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (6, 5, 6, 'いまひとつ。', 2, '2023-08-04', '2023-08-04' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (7, 5, 1, '美味しい！', 4, '2023-08-05', '2023-08-05' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (8, 5, 2, '味が素晴らしい', 5, '2023-08-09','2023-08-09' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (9, 5, 3, 'すごかった', 4, '2023-08-03','2023-08-04' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (10, 5, 4, '美味しかった。', 4, '2023-08-02','2023-08-02' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (11, 5, 5, '味がいまいち。', 1, '2023-08-02','2023-08-01' );
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, content, score, created_at, updated_at) VALUES (12, 5, 6, '味がうまい。', 3, '2023-08-02','2023-08-01' );

-- company_identifications テーブル
INSERT IGNORE INTO company_identifications (id, company_name, name, postal_code, address, business_content, created_at, updated_at) VALUES (1, 'NAGOYAMESHI株式会社', '侍　任三郎', '101-0022', '東京都千代田区神田練塀町300番地', '名古屋の飲食店の検索・予約サービス', '2023-08-03', '2023-08-04' );

-- terms_of_services テーブル
INSERT IGNORE INTO terms_of_services (id, article_1, article_2, article_3, created_at, updated_at) VALUES (1, '本規約は，ユーザーと当社との間の本サービスの利用に関わる一切の関係に適用されるものとします。', '当社は本サービスに関し，本規約のほか，ご利用にあたってのルール等，各種の定め（以下，「個別規定」といいます。）をすることがあります。これら個別規定はその名称のいかんに関わらず，本規約の一部を構成するものとします。', '本規約の規定が前条の個別規定の規定と矛盾する場合には，個別規定において特段の定めなき限り，個別規定の規定が優先されるものとします。
', '2023-08-03', '2023-08-04' );

-- reservationsテーブル
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (1, 1, 1, '2023-04-01', '19:00', 2, 6000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (2, 2, 1, '2023-04-04', '19:00', 3, 15000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (3, 3, 1, '2023-04-06', '19:00', 4, 4000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (4, 4, 1, '2023-04-08', '19:00', 5, 10000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (5, 5, 1, '2023-04-09', '19:00', 6, 12000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (6, 6, 1, '2023-04-10', '19:00', 2, 4000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (7, 7, 1, '2023-04-16', '19:00', 3, 3000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (8, 8, 1, '2023-04-19', '19:00', 4, 12000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (9, 9, 1, '2023-04-21', '19:00', 5, 15000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (10, 10, 1, '2023-04-26', '19:00', 3, 15000);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, reservation_date, reservation_time, number_of_people, amount) VALUES (11, 11, 1, '2023-04-27', '19:00', 2, 2000);

-- favoritesテーブル
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (1, 1, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (2, 2, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (3, 3, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (4, 4, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (5, 5, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (6, 6, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (7, 7, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (8, 8, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (9, 9, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (10, 10, 1);
INSERT IGNORE INTO favorites (id, restaurant_id, user_id) VALUES (11, 11, 1);
