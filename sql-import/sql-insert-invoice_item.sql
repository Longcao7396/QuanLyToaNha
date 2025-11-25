-- Xóa dữ liệu cũ
DELETE FROM invoice_item;

-- Nhập dữ liệu mới (chỉ 20 invoices đầu, mỗi invoice 3 items)
-- Sử dụng INSERT riêng cho từng item để đảm bảo invoice_id không null

-- Invoice 1 (apartment 01A01, tháng 1/2024)
INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A01' LIMIT 1) AND utility_type = 'ELECTRICITY' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Điện tháng 1/2024', 150, 3000, 450000
FROM invoice i WHERE i.invoice_number = 'INV-2024-001' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A01' LIMIT 1) AND utility_type = 'WATER' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Nước tháng 1/2024', 20, 20000, 400000
FROM invoice i WHERE i.invoice_number = 'INV-2024-001' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A01' LIMIT 1) AND utility_type = 'SERVICE_FEE' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Phí dịch vụ tháng 1/2024', 1, 500000, 500000
FROM invoice i WHERE i.invoice_number = 'INV-2024-001' LIMIT 1;

-- Invoice 2 (apartment 01A02, tháng 1/2024)
INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A02' LIMIT 1) AND utility_type = 'ELECTRICITY' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Điện tháng 1/2024', 200, 3000, 600000
FROM invoice i WHERE i.invoice_number = 'INV-2024-002' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A02' LIMIT 1) AND utility_type = 'WATER' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Nước tháng 1/2024', 25, 20000, 500000
FROM invoice i WHERE i.invoice_number = 'INV-2024-002' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A02' LIMIT 1) AND utility_type = 'SERVICE_FEE' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Phí dịch vụ tháng 1/2024', 1, 500000, 500000
FROM invoice i WHERE i.invoice_number = 'INV-2024-002' LIMIT 1;

-- Invoice 3 (apartment 01A03, tháng 1/2024)
INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A03' LIMIT 1) AND utility_type = 'ELECTRICITY' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Điện tháng 1/2024', 180, 3000, 540000
FROM invoice i WHERE i.invoice_number = 'INV-2024-003' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A03' LIMIT 1) AND utility_type = 'WATER' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Nước tháng 1/2024', 22, 20000, 440000
FROM invoice i WHERE i.invoice_number = 'INV-2024-003' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01A03' LIMIT 1) AND utility_type = 'SERVICE_FEE' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Phí dịch vụ tháng 1/2024', 1, 500000, 500000
FROM invoice i WHERE i.invoice_number = 'INV-2024-003' LIMIT 1;

-- Invoice 4 (apartment 01B01, tháng 1/2024)
INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01B01' LIMIT 1) AND utility_type = 'ELECTRICITY' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Điện tháng 1/2024', 220, 3000, 660000
FROM invoice i WHERE i.invoice_number = 'INV-2024-004' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01B01' LIMIT 1) AND utility_type = 'WATER' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Nước tháng 1/2024', 30, 20000, 600000
FROM invoice i WHERE i.invoice_number = 'INV-2024-004' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01B01' LIMIT 1) AND utility_type = 'SERVICE_FEE' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Phí dịch vụ tháng 1/2024', 1, 500000, 500000
FROM invoice i WHERE i.invoice_number = 'INV-2024-004' LIMIT 1;

-- Invoice 5 (apartment 01B02, tháng 1/2024)
INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01B02' LIMIT 1) AND utility_type = 'ELECTRICITY' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Điện tháng 1/2024', 160, 3000, 480000
FROM invoice i WHERE i.invoice_number = 'INV-2024-005' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01B02' LIMIT 1) AND utility_type = 'WATER' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Nước tháng 1/2024', 18, 20000, 360000
FROM invoice i WHERE i.invoice_number = 'INV-2024-005' LIMIT 1;

INSERT INTO invoice_item (invoice_id, utility_id, item_description, quantity, unit_price, amount)
SELECT 
    i.id,
    (SELECT id FROM utility WHERE apartment_id = (SELECT id FROM apartment WHERE apartment_no = '01B02' LIMIT 1) AND utility_type = 'SERVICE_FEE' AND period_month = 1 AND period_year = 2024 LIMIT 1),
    'Phí dịch vụ tháng 1/2024', 1, 500000, 500000
FROM invoice i WHERE i.invoice_number = 'INV-2024-005' LIMIT 1;
