
<script>
    var size = document.getElementById("meses").value;
    function myFunction() {

        var table = document.getElementById("myTable");

        var capital = parseFloat(document.getElementById("capital").value).toFixed(2) ;
        var juros = parseFloat(document.getElementById("juros").value).toFixed(1) ;


        var valorDeJuros = parseFloat(capital*juros/100).toFixed(2) ;
        var capitalEmDivida = parseFloat( +capital+ +valorDeJuros).toFixed(2);
        var amortizacao = parseFloat(capitalEmDivida/size).toFixed(2);
        for (i = 0; i < size; i++) {


            var row = table.insertRow(1);
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);
            var cell5 = row.insertCell(4);

            cell1.innerHTML = i+1;
            cell2.innerHTML = capital+"MT";
            cell3.innerHTML = juros+"MT";
            cell4.innerHTML = amortizacao+"MT";
            cell5.innerHTML = capitalEmDivida+"MT";

            capitalEmDivida-=parseFloat(amortizacao);
        }
        location.reload();
    }
    function clean(){

        for (i = 0; i < size; i++){
            document.getElementById("myTable").deleteRow(1);


        }

    }
</script>