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
    <style>
        div {
            /*background: #fc3; !* Цвет фона *!*/
            /*border: 2px solid black; !* Параметры рамки *!*/
            padding: 20px; /* Поля вокруг текста */
            margin-top: 5%; /* Отступ сверху */
        }
    </style>
    <style type="text/css">
        TABLE {
            width: 1700px; /* Ширина таблицы */
            border: 2px solid black; /* Рамка вокруг таблицы */
            /*background: #778899; !* Цвет фона *!*/
            color: black; /* Цвет текста */
        }
        TD, TH {
            text-align: right; /* Выравнивание по центру */
            padding: 3px; /* Поля вокруг текста */
        }
        TH {
            color: black; /* Цвет текста */
            border-bottom: 4px double black; /* Двойная линия снизу */
        }
        .even { /* Стиль для четных колонок */
            /*background: #ffe4b5; !* Цвет фона *!*/
            color: black; /* Цвет текста */
        }
        .lc { /* Стиль для первой колонки */
            text-align: left; /* Выравнивание по левому краю */
            color: black; /* Цвет текста */
        }
    </style>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

    <div>
        <form style="text-align: center" action="<%= request.getContextPath() %>/" method="post">
            <input type="hidden" name="action" value="return">
            <input type="submit" value="Назад"/>
        </form>
    </div>

    <form style="text-align: center" action="<%= request.getContextPath() %>/search" method="post">
        <input type="hidden" name="action" value="search">
        <input name="search" placeholder="Поиск">
    </form>


    <ul>
        <% if (request.getAttribute("catalog") != null) {%>
            <% for (Book item : (Collection<Book>) request.getAttribute("catalog")) { %>
                <table cellspacing="0">
                    <tr>
                        <td class="lc">
                            <%= item.getName() %>
                        </td>
                        <td class="even">
                            <a href="<%= request.getContextPath() %>/text/<%= item.getId() %>" download="<%= item.getName()%>.txt">Скачать файл</a>
                        </td>
                    </tr>
                </table>
            <% } %>
        <%}%>
    </ul>


</body>
</html>