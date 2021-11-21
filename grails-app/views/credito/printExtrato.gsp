<%--
  Created by IntelliJ IDEA.
  User: Fanisso
  Date: 9/29/2020
  Time: 8:49 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <script type="text/javascript">
        function loadExtrato() {
            window.open('/MALEYANGA/credito/printExtratoDeCredito', '_blank');
        }
    </script>
</head>

<body>
<button onclick="loadExtrato()"><glyph:icon iconName="printer"/>Extrato</button>

</body>
</html>