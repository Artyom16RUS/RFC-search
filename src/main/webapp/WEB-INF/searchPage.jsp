<%@ page import="model.Book" %>
<%@ page import="java.util.Collection" %>
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
    <form style="text-align: center" action="<%= request.getContextPath() %>/search" method="post">
        <input type="hidden" name="action" value="search">
        <input name="search" placeholder="Поиск">
    </form>

    <ul>
        <% if(request.getAttribute("catalog") != null){%>
        <% for (Book item : (Collection<Book>)request.getAttribute("catalog")) { %>
        <li>
            <%= item.getName() %> : <%= item.getId() %>
        </li>
        <% } %>
        <%}%>
    </ul>

    <form style="text-align: center" action="<%= request.getContextPath() %>/" method="post">
        <input type="hidden" name="action" value="return">
        <input type="submit" value="Назад"/>
    </form>
</body>
</html>