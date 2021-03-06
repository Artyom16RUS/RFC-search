<%@ page import="model.Document" %>
<%@ page import="java.util.Collection" %>
<%@ page import="Constant.Constant" %>
<%@ page import="Constant.ConstantJSP" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            padding: 5px; /* Поля вокруг текста */
            margin-top: 1%; /* Отступ сверху */
            margin-bottom: 1%;        }
    </style>
    <style type="text/css">
        TABLE {
            width: 1700px; /* Ширина таблицы */
            border: 2px solid gray; /* Рамка вокруг таблицы */
            /*background: #778899; !* Цвет фона *!*/
            color: black; /* Цвет текста */        }
        TD, TH {
            text-align: right; /* Выравнивание по центру */
            padding: 3px; /* Поля вокруг текста */        }
        TH {
            color: black; /* Цвет текста */
            border-bottom: 4px gray; /* Двойная линия снизу */        }
        .even { /* Стиль для четных колонок */
            /*background: #ffe4b5; !* Цвет фона *!*/
            color: black; /* Цвет текста */        }
        .lc { /* Стиль для первой колонки */
            text-align: left; /* Выравнивание по левому краю */
            color: black; /* Цвет текста */        }
    </style>
    <jsp:include page="style.jsp" />
</head>
<body>

<div>
    <form style="text-align: center" action="<%= request.getContextPath() %>/" method="get">
        <input type="hidden" name="action" value="return">
        <input type="submit" value="RFC Searcher"/>
    </form>
</div>

<jsp:include page="search.jsp"/>

<ul>
    <% if (request.getAttribute(ConstantJSP.CATALOG) != null) {%>
        <% for (Document item : (Collection<Document>) request.getAttribute(ConstantJSP.CATALOG)) { %>
        <table cellspacing="0">
            <tr>

                <% if (!item.getId().contains("0000-000000000000")) {%>
                <td class="lc">
                    <%= item.getName() %>
                </td>
                <td class="even">
                    <a href="<%= request.getContextPath() %>/download/<%= item.getId() %>" download="<%= item.getName()%>.txt">Скачать файл</a>
                </td>
                <%} else {%>
                <td class="lc">
                    <%= item.getName() %>
                </td>
                <td class="even">
                    <a>Не найдено</a>
                </td>
                <%}%>
            </tr>
        </table>
        <% } %>
    <%} %>
</ul>

</body>
</html>