<%@ page import="model.Book" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.io.Writer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Part part; %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Enrise</title>
    <style>
        div {
            /*background: #fc3; !* Цвет фона *!*/
            /*border: 2px solid black; !* Параметры рамки *!*/
            padding: 40px; /* Поля вокруг текста */
            margin-top: 10%; /* Отступ сверху */
        }
    </style>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

    <div>
        <form style="text-align: center" action="<%= request.getContextPath() %>/search" method="POST">
            <input type="hidden" name="action" value="search">
            <input name="search" placeholder="Поиск">
            <input type="submit" value="Найти">
        </form>
    </div>


    <form style="text-align: center" action="<%= request.getContextPath() %>/" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="save">
        <input type="file" multiple name="file" accept=".txt">
<%--        TODO все равно добавляется один фаил--%>
        <%--        Навзание книги: <input name="name"/>--%>
        <input type="submit" value="Добавить"/>
    </form>


    <ul>
        <% if (request.getAttribute("books") != null) {%>
            <% for (Book item : (Collection<Book>) request.getAttribute("books")) { %>
                <li>
                    <%= item.getName() %> : <%= item.getId() %>
                </li>
            <% } %>
        <%}%>
    </ul>



<%--    <form method="get">--%>
<%--        <label>Поиск по названию:--%>
<%--            <input type="text" name="search"><br />--%>
<%--        </label>--%>

<%--        <button type="submit">Поиск</button>--%>
<%--    </form>--%>


    </body>
</html>
