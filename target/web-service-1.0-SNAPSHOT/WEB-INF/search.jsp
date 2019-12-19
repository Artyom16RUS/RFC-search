<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--<div>--%>
<%--    <form style="text-align: center" action="<%= request.getContextPath() %>/search" method="post">--%>
<%--        <input type="hidden" name="action" value="search">--%>
<%--        <input name="search" placeholder="Поиск" maxlength="50" type="text" required>--%>
<%--        <input type="submit" value="Найти">--%>
<%--    </form>--%>
<%--</div>--%>

<div>
    <form method="POST" enctype="multipart/form-data" action="<%= request.getContextPath() %>/search">
        <table>
            <tr>
                <td>File to upload:</td>
                <td>
                    <input type="file" name="file" />
                </td>
            </tr>
            <tr>
                <td>

                </td>
                <td><input type="submit" value="Upload" /></td>
            </tr>
        </table>
    </form>
</div>