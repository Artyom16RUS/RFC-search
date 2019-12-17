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
            margin-bottom: 1%;
        }
    </style>
    <jsp:include page="style.jsp" />
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col" style="text-align: center">
                <h1>Что то пошло нет так</h1>
            </div>
        </div>
    </div>
</body>

<div>
    <form style="text-align: center" action="<%= request.getContextPath() %>/" method="post">
        <input type="hidden" name="action" value="return">
        <input type="submit" value="RFC Searcher"/>
    </form>
</div>
</html>

