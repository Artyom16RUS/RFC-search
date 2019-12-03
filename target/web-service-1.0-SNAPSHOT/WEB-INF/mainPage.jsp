<%@ page import="model.Book" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.io.Writer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Enrise</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

<form action="<%= request.getContextPath() %>/" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="save">
    Навзание книги: <input name="name"/>
    <input type="file" name="file" accept="image/*">
    <input type="submit" value="Добавить"/>
</form>


    <ul>
        <% if(request.getAttribute("books") != null){%>
            <% for (Book item : (Collection<Book>)request.getAttribute("books")) { %>
            <li>
                <%= item.getName() %>
            </li>
            <% } %>
        <%}%>
    </ul>

    <form action="<%= request.getContextPath() %>/" method="post">
        <input type="hidden" name="action" value="search">
        <input name="search" placeholder="Поиск">
        <%-- если поле всего одно, то Enter приводит к отправке формы --%>
    </form>

    <ul>
        <% if(request.getAttribute("catalog") != null){%>
        <% for (Book item : (Collection<Book>)request.getAttribute("catalog")) { %>
        <li>
            <%= item.getName() %>
        </li>
        <% } %>
        <%}%>
    </ul>
</body>
</html>