package com.shopme.admin.user.service.export;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "users_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "Email", "Firstname", "Lastname", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstname", "lastname", "roles", "enabled"};

        csvBeanWriter.writeHeader(csvHeader);

        for (User user : listUsers) {
            csvBeanWriter.write(user, fieldMapping);
        }

        csvBeanWriter.close();
    }
}
