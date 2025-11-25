-- Xóa dữ liệu cũ
DELETE FROM invoice;

-- Nhập dữ liệu mới (chỉ 20 records đầu)
-- Lưu ý: created_by sẽ sử dụng admin user
INSERT INTO invoice (apartment_id, invoice_number, invoice_date, due_date, total_amount, paid_amount, remaining_amount, status, payment_method, payment_date, notes, created_by) VALUES
((SELECT id FROM apartment WHERE apartment_no = '01A01'), 'INV-2024-001', '2024-01-05', '2024-01-20', 1350000, 1350000, 0, 'PAID', 'BANK_TRANSFER', '2024-01-15', 'Hóa đơn tháng 1', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '01A02'), 'INV-2024-002', '2024-01-05', '2024-01-20', 1600000, 1600000, 0, 'PAID', 'BANK_TRANSFER', '2024-01-16', 'Hóa đơn tháng 1', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '01A03'), 'INV-2024-003', '2024-01-05', '2024-01-20', 1480000, 1480000, 0, 'PAID', 'CASH', '2024-01-17', 'Hóa đơn tháng 1', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '01B01'), 'INV-2024-004', '2024-01-05', '2024-01-20', 1760000, 1760000, 0, 'PAID', 'BANK_TRANSFER', '2024-01-18', 'Hóa đơn tháng 1', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '01B02'), 'INV-2024-005', '2024-01-05', '2024-01-20', 1340000, 1340000, 0, 'PAID', 'CARD', '2024-01-19', 'Hóa đơn tháng 1', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '02A01'), 'INV-2024-006', '2024-02-05', '2024-02-20', 1350000, 1350000, 0, 'PAID', 'BANK_TRANSFER', '2024-02-15', 'Hóa đơn tháng 2', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '02A02'), 'INV-2024-007', '2024-02-05', '2024-02-20', 1600000, 1600000, 0, 'PAID', 'BANK_TRANSFER', '2024-02-16', 'Hóa đơn tháng 2', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '02A03'), 'INV-2024-008', '2024-02-05', '2024-02-20', 1480000, 1480000, 0, 'PAID', 'CASH', '2024-02-17', 'Hóa đơn tháng 2', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '02B01'), 'INV-2024-009', '2024-02-05', '2024-02-20', 1760000, 1760000, 0, 'PAID', 'BANK_TRANSFER', '2024-02-18', 'Hóa đơn tháng 2', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '02B02'), 'INV-2024-010', '2024-02-05', '2024-02-20', 1340000, 1340000, 0, 'PAID', 'CARD', '2024-02-19', 'Hóa đơn tháng 2', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '03A01'), 'INV-2024-011', '2024-03-05', '2024-03-20', 1350000, 1350000, 0, 'PAID', 'BANK_TRANSFER', '2024-03-15', 'Hóa đơn tháng 3', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '03A02'), 'INV-2024-012', '2024-03-05', '2024-03-20', 1600000, 1600000, 0, 'PAID', 'BANK_TRANSFER', '2024-03-16', 'Hóa đơn tháng 3', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '03A03'), 'INV-2024-013', '2024-03-05', '2024-03-20', 1480000, 1480000, 0, 'PAID', 'CASH', '2024-03-17', 'Hóa đơn tháng 3', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '03B01'), 'INV-2024-014', '2024-03-05', '2024-03-20', 1760000, 1760000, 0, 'PAID', 'BANK_TRANSFER', '2024-03-18', 'Hóa đơn tháng 3', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '03B02'), 'INV-2024-015', '2024-03-05', '2024-03-20', 1340000, 1340000, 0, 'PAID', 'CARD', '2024-03-19', 'Hóa đơn tháng 3', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '04A01'), 'INV-2024-016', '2024-04-05', '2024-04-20', 1350000, 1350000, 0, 'PAID', 'BANK_TRANSFER', '2024-04-15', 'Hóa đơn tháng 4', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '04A02'), 'INV-2024-017', '2024-04-05', '2024-04-20', 1600000, 1600000, 0, 'PAID', 'BANK_TRANSFER', '2024-04-16', 'Hóa đơn tháng 4', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '04A03'), 'INV-2024-018', '2024-04-05', '2024-04-20', 1480000, 1480000, 0, 'PAID', 'CASH', '2024-04-17', 'Hóa đơn tháng 4', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '04B01'), 'INV-2024-019', '2024-04-05', '2024-04-20', 1760000, 1760000, 0, 'PAID', 'BANK_TRANSFER', '2024-04-18', 'Hóa đơn tháng 4', (SELECT id FROM user WHERE username = 'admin' LIMIT 1)),
((SELECT id FROM apartment WHERE apartment_no = '04B02'), 'INV-2024-020', '2024-04-05', '2024-04-20', 1340000, 1340000, 0, 'PAID', 'CARD', '2024-04-19', 'Hóa đơn tháng 4', (SELECT id FROM user WHERE username = 'admin' LIMIT 1));
