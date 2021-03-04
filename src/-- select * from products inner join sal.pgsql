-- select * from products inner join sales ON products.id = sales.product_id;

-- select  from products inner join sales ON products.id = sales.product_id group by (quantity_in_store);

-- select * from products 
-- inner join 
-- (select products.id as pid,  products.quantity_in_store-sum(quantity) as tempquantity from sales  inner join products ON products.id = sales.product_id group by(products.id)) as d
-- on products.id = pid ;





CREATE OR REPLACE FUNCTION set_ean() RETURNS TRIGGER AS $$
DECLARE
    new_only_number_length INTEGER;
    ean_l INTEGER;
    new_ean VARCHAR;
BEGIN
        SELECT FLOOR(LOG(NEW.id) + 1) INTO new_only_number_length;
        new_ean = (NEW.id)::CHARACTER VARYING;
        ean_l = 13 - new_only_number_length;

        while ean_l > 0 loop
            new_ean = '0' || new_ean;
          ean_l = ean_l - 1;
        end loop;
        UPDATE products SET ean = new_ean where id =  NEW.id;
        RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER on_product_insert
AFTER INSERT ON products 
FOR EACH ROW EXECUTE PROCEDURE set_ean();

insert into products (title, price, measure, quantity_in_store) values ('Realme Snapdragon 855 Plus 256GB 12gbb LTE/WCDMA/GSM', 13, 'PIECE', 5938); 
insert into products (title, price, measure, quantity_in_store) values ('Тактический ремень тканевый LEMON Хаки tkn-40-tkt-004', 43, 'PIECE', 213); 
insert into products (title, price, measure, quantity_in_store) values ('Кастрюля нержавеющая Maestro', 543, 'PIECE', 4356); 
insert into products (title, price, measure, quantity_in_store) values ('Компьютерное офисное кресло MARSEILLE/BL 4301 brown', 32, 'PIECE', 10021); 
insert into products (title, price, measure, quantity_in_store) values ('Беговая дорожка EnergyFIT 815 new (818)', 44, 'PIECE', 323); 
insert into products (title, price, measure, quantity_in_store) values ('Кухонная мойка VANKOR Hope HMP 02.57 Beige', 65, 'PIECE', 534); 
insert into products (title, price, measure, quantity_in_store) values ('Набор PLA пластика для 3D-ручки Myflament 70 метров', 34, 'PIECE', 43); 
insert into products (title, price, measure, quantity_in_store) values ('Реле давления Насосы+Оборудование PS-II-15', 654, 'PIECE', 234); 
insert into products (title, price, measure, quantity_in_store) values ('Дренажно-фекальный насос Sprut V180F', 32, 'PIECE', 54); 
insert into products (title, price, measure, quantity_in_store) values ('Насос циркуляционный Powercraft DCA 25-4-180', 65, 'PIECE', 564); 
insert into products (title, price, measure, quantity_in_store) values ('Кресло Новый Стиль Alfa GTP C-11', 12, 'PIECE', 345); 
insert into products (title, price, measure, quantity_in_store) values ('Кресло Новый Стиль Ministyle GTS White Cat and Mouse', 16, 'PIECE', 234); 
insert into products (title, price, measure, quantity_in_store) values ('Автосканер Epitek ELM327 v1.5 Bluetooth', 17, 'PIECE', 543); 
insert into products (title, price, measure, quantity_in_store) values ('Инфракрасный обогреватель RZTK QH 1512H', 13, 'PIECE', 765); 
insert into products (title, price, measure, quantity_in_store) values ('Электрическая зубная щетка PECHAM Black Travel', 59, 'PIECE', 534); 
insert into products (title, price, measure, quantity_in_store) values ('Клавиатура проводная Real-El 8700 Backlit USB', 57, 'PIECE', 23); 
insert into products (title, price, measure, quantity_in_store) values ('Оперативная память HyperX DDR4-3200 16384MB PC4-25600 (Kit of 2x8192) Fury Black', 36, 'PIECE', 54); 
insert into products (title, price, measure, quantity_in_store) values ('Мобильный телефон Xiaomi Redmi Note 9 Pro 6/128GB Tropical Green', 20, 'PIECE', 10650); 


select * from products;
