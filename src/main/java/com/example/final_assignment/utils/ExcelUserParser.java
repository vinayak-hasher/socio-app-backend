package com.example.final_assignment.utils;

import com.example.final_assignment.entities.User;
import com.example.final_assignment.entities.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUserParser {

    public static List<User> parse(InputStream input) throws IOException {
        List<User> users = new ArrayList<>();

        log.info("Parsing Started");
        try (Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String name = getCellValue(row.getCell(0));
                String email = getCellValue(row.getCell(1));
                String password = getCellValue(row.getCell(2));
                String roleStr = getCellValue(row.getCell(3)).toUpperCase();

                Role role;
                if (roleStr.equals("ADMIN")) {
                    role = Role.ADMIN;
                } else {
                    role = Role.USER;
                }

                User user = User.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .role(role)
                        .build();
                users.add(user);
            }
        }
        return users;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return cell.toString();
    }
}

