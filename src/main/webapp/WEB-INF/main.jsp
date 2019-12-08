<%@ page import="model.Document" %>
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
    <title>RFC Searcher</title>
    <style>
        div {
            /*background: #fc3; !* Цвет фона *!*/
            /*border: 2px solid black; !* Параметры рамки *!*/
            padding: 40px; /* Поля вокруг текста */
            margin-top: 10%; /* Отступ сверху */
        }
    </style>
    <jsp:include page="style.jsp" />
</head>
<body>

    <jsp:include page="search.jsp" />


    <form style="text-align: center" action="<%= request.getContextPath() %>/" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="save">
        <input type="file" name="file" accept=".txt" multiple required>
        <input type="submit" value="Добавить"/>
    </form>

    <form style="text-align: center">
        <ul>
            <% if (request.getAttribute("statusAdd") != null) {%>
                <% if(((Collection<Document>) request.getAttribute("statusAdd")).size() == 1) {%>
                    <p>File added</p>
                <%} if(((Collection<Document>) request.getAttribute("statusAdd")).size() > 1)  {%>
                    <p>Files added</p>
                <% } %>
            <%}%>
        </ul>
    </form>
    <form style="text-align: center">
        <ul>
            <% if (request.getAttribute("statusNotAdd") != null) {%>
                <% for (String item : (Collection<String>) request.getAttribute("statusNotAdd")) { %>
                    <%= item %>
                <% } %>
            <%}%>
        </ul>
    </form>

    </body>
</html>
