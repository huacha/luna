package com.luna.common.utils.export;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface Exportor {
    void export(HttpServletResponse response, TableModel tableModel)
            throws IOException;
}
