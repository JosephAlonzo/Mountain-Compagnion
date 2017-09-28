function fibonacciRecursive(n){
            
            if(n == 0){
                return 0
            }
            else if (n == 1)
                {
                    return 1
                }
            else {
                return fibonacciRecursive(n-1) + fibonacciRecursive(n-2);
            }
        }
        function fibonacciNumerique(n)
        {
            var fib1; 
            var fib2;
            var resul ;
            for(var i = 0; i <= n ; i++)
                {
                    if (i == 0)
                        {
                         fib1 = 0;
                         fib2 = 0;
                            resul = 0;
                        }
                    else if (i == 1 || i == 2)
                        {
                            fib1 = 1;
                            fib2 = 1;
                            resul = 1;
                        }
                    else{
                        resul = fib1 + fib2;
                        fib1 = fib2;
                        fib2 = resul;
                    }
                    
                }
            return resul;
        }
        function SearchValue()
        {
        
                var start = new Date().getDate();

           
            var Nombre = document.getElementById("valeur").value;
           
            if( Entier(Nombre))
                {
            if(document.getElementById("Recursive").checked)
                {
                  var stopTimer = new Date().getTime() - start;

                alert("la fonction Fibonacci d'ordre  " + Nombre  + " es égale à "+ fibonacciRecursive(Nombre) +
                " Temps d'exécution : "+ stopTimer + " secondes");
                }
            else{
                 var stopTimer = new Date().getTime() - start;

                alert("la fonction Fibonacci d'ordre  " + Nombre  + " es égale à "+ fibonacciNumerique(Nombre) +
                " Temps d'exécution : "+ stopTimer + " secondes");
            }
                }
             else {
                
                alert("Le Nombre doit etre positif et entier")
            }
        }
        function Entier(nombre){
            if (isNaN(nombre)){
               return false;
            }
            else{
                if (nombre % 1 == 0) {
                    return true;
                }
                else{
                    return false;
                }
            }
        }