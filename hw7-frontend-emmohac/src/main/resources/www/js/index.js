$(document).ready(function () {
    var sessionID; //global sessionID
    var email; //global email for user
    var title;
    var director;
    var year;
    var genre;
    var direction;
    var orderby;

    var globalOffset = 0;
    var globalLimit = 10;
    function handleError(xhr, status, error) {
        // console.log(xhr);
        // console.log(status);
        // console.log(error);
    }

    function handleSuccess(result, textstatus, xhr){
        //console.log("handleSuccess sessionID: "+xhr.getResponseHeader("sessionID"));
        //console.log("handleSuccess email: "+email);

        sessionID = xhr.getResponseHeader("sessionID");
        if (result["resultCode"] == 120) {
            sessionID = result["sessionID"];
            console.log("sessionID from login: "+sessionID);
            $("#welcome").empty();
            $("#welcome").append("Welcome, "+email);
            setTimeout(function(){
                $('#advancedSearch').trigger('click');
            }, 1000);
        }
        let message = $(".error");
        message.empty();
        message.append(result["message"]);
        $(".getMovies").empty();
        $(".movies").empty();

        if (result["resultCode"] == 3410){
            //console.log("Going to order history...");
            console.log("THIS IS RESULT: "+result["transactions"]);
            let page = $(".page");
            page.empty();
            let numOfTrans = result.transactions.length;
            let orderHistory = "<center><table border='1'><tr><th>Transaction ID</th><th>Order status</th><th>Total Price</th><th>Item</th>";
            for (let i = 0; i < numOfTrans; ++i){
                orderHistory += "<tr>";
                orderHistory += "<td>" + (result.transactions)[i].transactionId + "</td>";
                orderHistory += "<td>" + (result.transactions)[i].state + "</td>";
                orderHistory += "<td>" + (result.transactions)[i].amount.total + " " + (result.transactions)[i].amount.currency + "</td>";

                let numOfItems = result.transactions[i].items.length;
                let items = "";
                for (let j = 0; j < numOfItems; ++j){
                    items += "Movie ID: " + (result.transactions)[i].items[j].movieId + "<br>";
                    items += "Quantity: " + (result.transactions)[i].items[j].quantity + "<br>";
                    items += "Unit Price: " + (result.transactions)[i].items[j].unit_price + "<br>";
                    items += "Discount: " + (result.transactions)[i].items[j].discount + "<br>";
                    items += "Sale Date: " + (result.transactions)[i].items[j].saleDate + "<br>";
                }
                orderHistory += "<td>" +items + "</td>";
                orderHistory += "</tr>";

            }
            orderHistory += "</table></center>";
            page.append(orderHistory);
        }
        else if (result["resultCode"] == 3130){
            let cart = $(".shoppingcart");
            cart.empty();
            let items = result["items"];
            let len = items.length;
            let row = "<table border=\"1\"><tr><th>Email</th><th>Movie ID</th><th>Quantity</th><th>Bye Movie</th>";
            for (let i = 0; i < len; ++i){
                row += "<tr>";
                row += "<td>"+items[i].email+"</td>";
                row += "<td>"+items[i].movieId+"</td>";
                row += "<td>"+items[i].quantity+"</td>";
                row += "<td><button class='deleteMovie' id="+items[i].movieId+">Delete</button></td>";
                row += "</tr>";
            }
            row += "</table>";
            cart.append(row);

            $(".deleteMovie").click(function (e) {
                e.preventDefault();
                let movieID = this.id;
                let userEmail = email;
                console.log("Trying to delete: "+movieID);
                let requestModel = {email: userEmail, movieId: movieID};

                $.ajax({
                    url: "http://127.0.0.1:2328/api/g/billing/cart/delete",
                    method: "POST",
                    contentType: "application/json",
                    crossDomain: true,
                    headers: {
                        "Access-Control-Expose-Headers": "*",
                        "Access-Control-Allow-Origin": "*",
                        "sessionID": sessionID,
                        "email": email
                    },
                    data: JSON.stringify(requestModel),
                    success: handleNoContent,
                    statusCode: {
                        400: handleError,
                        500: handleError
                    }
                });
            });
        }
        else if (result["resultCode"] == 3110) $(".showCart").trigger('click');
        else if (result["resultCode"] == 3140) $(".shoppingcart").empty();
        else if (result["resultCode"] == 3400){
            //console.log("Code 3400");
            let urlPaypal = result["redirectURL"];
            console.log("RedirectURL: "+urlPaypal);
            window.open(urlPaypal);
        }else if (result["resultCode"] == 3300 || result["resultCode"] == 333) setTimeout(realCheckOut, 500);
        else if (result["resultCode"] == 133) {
            $("#regLogUser").trigger('click' );
        }
        else if (result["resultCode"] == 3120){
            $(".showCart").trigger('click');
        }
        else if (result["resultCode"] == 3100){
            $(".showCart").trigger('click');
        }
    }

    function handleNoContent(result, textstatus, xhr){
        let transactionID = xhr.getResponseHeader("transactionID");
        if (xhr.status == 204){
            $.ajax({
                url: "http://127.0.0.1:2328/api/g/report",
                method: "GET",
                contentType: "application/json",
                crossDomain: true,
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID,
                    "sessionID": sessionID,
                    "email": email
                },
                success: handleNoContent,
                statusCode: {
                    400: handleError(result, textstatus, xhr),
                    500: handleError(result, textstatus, xhr)
                }
            });
        }
        else if (xhr.status == 200)
            handleSuccess(result, textstatus, xhr);
        else
            handleError(result, textstatus, xhr);
    }

    function handleMovieNoContent(result, textstatus, xhr){
        let transactionID = xhr.getResponseHeader("transactionID");
        //sessionID = xhr.getResponseHeader("sessionID");
        if (xhr.status == 204){
            console.log("Status Code: "+xhr.status);
            $.ajax({
                url: "http://127.0.0.1:2328/api/g/report",
                method: "GET",
                contentType: "application/json",
                crossDomain: true,
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID,
                    "sessionID": sessionID,
                    "email": email
                },
                success: handleMovieNoContent,
                statusCode: {
                    400: handleError(result, textstatus, xhr),
                    500: handleError(result, textstatus, xhr)
                }
            });
        }
        else if (xhr.status == 200){
            //console.log("handleMovieNoContent: 200");
            //console.log("Trying to go to handleMovieSuccess");
            $.ajax({
                url: "http://127.0.0.1:2328/api/g/report",
                method: "GET",
                contentType: "application/json",
                crossDomain: true,
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID,
                    "sessionID": sessionID,
                    "email": email
                },
                success: handleMovieSuccess
            });
        }
        else
            console.log("Error...");
    }

    function handleMovieSuccess(response){
        //console.log("handleMovieSuccess...\n");
        let movieDom = $(".movies");
        movieDom.empty();
        let row = "<center><table border=\"1\"><tr><th>Movie ID</th><th>Title</th><th>Director</th><th>Year</th><th>Rating</th><th>Number of vote</th>";
        let movies = response["movies"];
        let message = response["message"];
        let error = $(".error");
        error.empty();
        error.append(message);
        let movieLength = movies.length;
        console.log("Number of movie: " +movieLength);
        for (let i = 0; i < movieLength; ++i){
            row += "<tr>";
            let theMovie = movies[i];
            let movieID = theMovie["movieId"];
            row += "<td>" + theMovie["movieId"] + "<input class='showDetail' id="+movieID+" type=\"submit\" value='detail'/></td>";
            row += "<td>" + theMovie["title"] + "</td>";
            row += "<td>" + theMovie["director"] + "</td>";
            row += "<td>" + theMovie["year"] + "</td>";
            row += "<td>" + theMovie["rating"] + "</td>";
            row += "<td>" + theMovie["numVotes"] + "</td>";
            row += "</tr>";
        }
        row += "</table></center>";
        let pagination = "<div><button class='previous'>Previous</button><button class='next'>Next</button></div>";
        movieDom.append(row);
        movieDom.append(pagination);

        $(".previous").click(function (e) {
            e.preventDefault();

            console.log("Previous button clicked...");
            $(".getMovies").empty();
            //globalOffset = parseInt(globalOffset - globalLimit);
            globalOffset = globalOffset - globalLimit;
            if (globalOffset < 0) globalOffset = 0;
            console.log("LIMIT in previous: "+globalLimit);
            console.log("OFFSET in previous: "+globalOffset);
            let urlBuilder = "http://127.0.0.1:2328/api/g/movies/search?title="+title+
                "&director="+director+"&year="+year+"&genre="+genre+
                "&offset="+globalOffset+"&limit="+globalLimit+"&orderby="+orderby+
                "&direction="+direction;
            urlBuilder = urlBuilder.replace(" ", "%20");

            $.ajax({
                url: urlBuilder,
                method: "GET",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                crossDomain: true,
                success: function(result, textstatus, xhr){
                    if (xhr.status == 204)
                        handleMovieNoContent(result, textstatus, xhr);
                    else if (xhr.status == 200)
                        handleMovieSuccess;
                    else
                        handleError;
                },
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            })
        });

        $(".next").click(function (e) {
            e.preventDefault();

            console.log("Previous button clicked...");
            $(".getMovies").empty();
            if (globalOffset < 0) globalOffset = 0;
                globalOffset = Number(globalOffset)+Number(globalLimit);

            console.log("LIMIT in next: "+globalLimit);
            console.log("OFFSET in next: "+globalOffset);

            let urlBuilder = "http://127.0.0.1:2328/api/g/movies/search?title="+title+
                "&director="+director+"&year="+year+"&genre="+genre+
                "&offset="+globalOffset+"&limit="+globalLimit+"&orderby="+orderby+
                "&direction="+direction;
            urlBuilder = urlBuilder.replace(" ", "%20");

            $.ajax({
                url: urlBuilder,
                method: "GET",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                crossDomain: true,
                success: function(result, textstatus, xhr){
                    if (xhr.status == 204)
                        handleMovieNoContent(result, textstatus, xhr);
                    else if (xhr.status == 200)
                        handleMovieSuccess;
                    else
                        handleError;
                },
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            })
        });

        $(".showDetail").click(function (e){
            e.preventDefault();
            let movieID = this.id;
            console.log("detail button clicked...");

            $.ajax({
                url: "http://127.0.0.1:2328/api/g/movies/get/"+movieID,
                method: "GET",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "sessionID": sessionID,
                    "email": email
                },
                crossDomain: true,
                success: handleGetMovieNoContent,
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            });
        });
    }

    function handleGetMovieNoContent(result, text, xhr){
        console.log("handleGetMovieNoContent");
        let transactionID = xhr.getResponseHeader("transactionID");

        if (xhr.status == 204){
            $.ajax({
                url: "http://127.0.0.1:2328/api/g/report",
                method: "GET",
                contentType: "application/json",
                crossDomain: true,
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID,
                    "sessionID": sessionID,
                    "email": email
                },
                success: handleGetMovieNoContent,
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            });
        }
        else if (xhr.status == 200){
            $.ajax({
                url: "http://127.0.0.1:2328/api/g/report",
                method: "GET",
                contentType: "application/json",
                crossDomain: true,
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "transactionID": transactionID,
                    "email": email,
                    "sessionID": sessionID
                },
                success: handleGetMovieSuccess,
            })
        }
        else
            console.log("Error...")
    }

    function handleGetMovieSuccess(res, text, xhr){
        let getDom = $(".getMovies");
        getDom.empty();
        let movie = res["movie"];
        console.log(movie);
        let row = "<center><table border=\"1\"><tr><th>Movie ID</th><th>Title</th><th>Director</th><th>Year</th><th>Backdrop Path</th><th>Overview</th><th>Poster Path</th><th>Revenue</th><th>Rating</th><th>Number of vote</th><th>Genre</th><th>Star</th>";
        row += "<tr>";
        row += "<td>"+movie["id"]+"</td>";
        row += "<td>" + movie["title"] + "</td>";
        row += "<td>" + movie["director"] + "</td>";
        row += "<td>" + movie["year"] + "</td>";
        row += "<td>" + movie["backdrop_path"] + "</td>";
        row += "<td>" + movie["overview"] + "</td>";
        row += "<td>" + movie["poster_path"] + "</td>";
        row += "<td>" + movie["revenue"] + "</td>";
        row += "<td>" + movie["rating"] + "</td>";
        row += "<td>" + movie["numVotes"] + "</td>";
        let genreString = "";
        let genreLen = movie["genres"].length;

        for (let i = 0; i < genreLen; ++i){
            genreString += movie["genres"][i].name+", ";
        }
        row += "<td>" + genreString + "</td>";

        let starString = "";
        let starLen = movie["stars"].length;
        for (let i = 0; i < starLen; ++i){
            starString += movie["stars"][i].name+", ";
        }
        row += "<td>" + starString + "</td>";
        row += "</table></center>";
        getDom.append(row);

        let movieSale = "<form action='#'><div><input class='quantity' type='number' placeholder='quantity'/><button class='insertCart' type='submit'>Add to cart</button></div>";
        movieSale += "<div><input class='rating' type='number' placeholder='rating'/><button class='rateMovie' type='submit'>Rate</button></div>";
        movieSale += "<div><input class='update' type='number' placeholder='new quantity'/><button class='updateCart' type='submit'>Update Cart</button></div></form>";
        getDom.append(movieSale);
        let transactionID = xhr.getResponseHeader("transactionID");
        console.log("handleGetMovieSuccess: transactionID is "+transactionID);
        console.log("handleGetMovieSuccess: sessionID is"+sessionID);

        $(".rateMovie").click(function (e){
            e.preventDefault();

            let theID = movie["id"];
            let movieRating = $(".rating").val();
            let requestModel = {id: theID, rating: movieRating};

            $.ajax({
                url: "http://127.0.0.1:2328/api/g/movies/rating",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(requestModel),
                crossDomain: true,
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
               success: handleNoContent,
               statusCode: {
                    400: handleError,
                    500: handleError
               }
            });
        });

        $(".updateCart").click(function (e) {
           e.preventDefault();
           let id = movie["id"];
           let newQuantity = $(".update").val();
           let userEmail = email;

           let requestModel = {email: userEmail, movieId: id, quantity: newQuantity};

           $.ajax({
               url: "http://127.0.0.1:2328/api/g/billing/cart/update",
               method: "POST",
               contentType: "application/json",
               data: JSON.stringify(requestModel),
               crossDomain: true,
               headers: {
                   "Access-Control-Expose-Headers": "*",
                   "Access-Control-Allow-Origin": "*",
                   "email": email,
                   "sessionID": sessionID
               },
               success: handleNoContent,
               statusCode: {
                   400: handleError,
                   500: handleError
               }
           })
        });

        $(".insertCart").click(function (e){
            e.preventDefault();
            let id = movie["id"];
            let theQuantity = $(".quantity").val();
            let userEmail = email;
            console.log("Insert cart sessionID: "+sessionID);

            let requestModel = {
                email: userEmail,
                movieId: id,
                quantity: theQuantity
            };

            $.ajax({
                url: "http://127.0.0.1:2328/api/g/billing/cart/insert",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(requestModel),
                crossDomain: true,
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                success: handleNoContent,
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            })
        });
    }

    $(".showCart").click(function (e){
        e.preventDefault();
        if (!sessionID){
            console.log("SessionID not provided.");
            $("#regLogUser").trigger('click');
        }
        let userEmail = email;
        $(".shoppingcart").empty();
        let requestModel = {email: userEmail};
        console.log("show cart sessionID: "+sessionID);
        $.ajax({
            url: "http://127.0.0.1:2328/api/g/billing/cart/retrieve",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestModel),
            crossDomain: true,
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*",
                "email": email,
                "sessionID": sessionID
            },
            success: handleNoContent,
            statusCode: {
                400: handleError,
                500: handleError
            }
        })
    });

    $("#regLogUser").click(function (e) {
        e.preventDefault();
        console.log("Register/Login Page clicked...");
        let registerPage = $(".page");
        registerPage.empty();
        globalOffset = 0;
        globalLimit = 10;
        $(".movies").empty();
        $(".error").empty();
        let getDom = $(".getMovies");
        getDom.empty();
        let register = "<form class=\"box\" action=\"#\"><h1><b>Register/Login Page</b></h1>";
        register += "<input type=\"email\" class=\"email\" placeholder=\"email\" required/>";
        register += "<input type=\"text\" class=\"password\" placeholder=\"password\" required/>";
        register += "<input type=\"submit\" class='register' value=\"Register\"/>";
        register += "<input type=\"submit\" class='login' value=\"Login\" /></form>";
        registerPage.append(register);

        $(".register").click(function (e) {
            console.log("register button clicked.");
            e.preventDefault();

            let theEmail = $(".email").val();
            let thePassword = $(".password").val();

            console.log("Register: \n");
            console.log("Email: " + theEmail);
            console.log("Password: " + thePassword);
            let registerRequestModel = {email: theEmail, password: thePassword};
            //email = theEmail;

            $.ajax({
                url: "http://127.0.0.1:2328/api/g/idm/register",
                method: "POST",
                data: JSON.stringify(registerRequestModel),
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*"
                },
                crossDomain: true,
                success: function(result, textstatus, xhr){
                    if(xhr.status == 200)
                        handleSuccess(result, textstatus, xhr);
                    else if (xhr.status == 204)
                        handleNoContent(result, textstatus, xhr);
                    else
                        handleError(result, textstatus, xhr);
                },
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            });
        });

        $(".login").click(function (e) {
            console.log("login button clicked.");
            e.preventDefault();

            let theEmail = $(".email").val();
            let thePassword = $(".password").val();
            let loginRequestModel = {email: theEmail, password: thePassword};
            email = theEmail;
            console.log("Login: \n");
            console.log("Email: " + theEmail);
            console.log("Password: " + thePassword);
            $.ajax({
                url: "http://127.0.0.1:2328/api/g/idm/login",
                method: "POST",
                data: JSON.stringify(loginRequestModel),
                contentType: "application/json",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*"
                },
                crossDomain: true,
                success: function(result, textstatus, xhr){
                    if (xhr.status == 200)
                        handleSuccess(result, textstatus, xhr);
                    else if (xhr.status == 204)
                        handleNoContent(result, textstatus, xhr);
                    else
                        handleError(result, textstatus, xhr);
                },
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            })
        })

    });

    $("#advancedSearch").click(function (e){
        e.preventDefault();
        console.log("Advanced Search Page clicked");
        if (!sessionID){
            alert("Need to login first");
            setTimeout(function(){
                $("#regLogUser").trigger('click');
            }, 500);
        }
        console.log("Global email: "+email);
        console.log("Global sessionID: "+sessionID);
        globalLimit = 10;
        globalOffset = 0;
        $(".getMovies").empty();
        $(".movies").empty();
        let advancedSearch = $(".page");
        advancedSearch.empty();
        $(".error").empty();
        $(".rating").empty();
        $(".update").empty();
        $(".quantity").empty();
        let frame = "<div style=\"text-align: center;\" class=\"advancedSearch\" >";
        frame += "<form action=\"#\"><h1><strong>Movie Search</strong></h1>";
        frame += "<div><input type=\"text\" class=\"advancedTitle\" placeholder='title' required/></div>";
        frame += "<div><input type=\"text\" class=\"director\" placeholder='director' required/></div>";
        frame += "<div><input type=\"number\" class=\"year\" placeholder='year' required/></div>";
        frame += "<div><input type=\"text\" class=\"genre\" placeholder='genre' required/></div>";
        frame += "<div><input type=\"text\" class=\"orderby\" placeholder='orderby' required/></div>";
        frame += "<div><input type=\"text\" class=\"direction\" placeholder='direction' required/></div>";
        frame += "<div><input type=\"number\" class=\"limit\" placeholder='limit' /></div>";
        frame += "<div><input type=\"number\" class=\"offset\" placeholder='offset'/></div>";
        frame += "<div><input type=\"submit\" class='detailSearch' value='Advanced Search'/></form></div>";
        advancedSearch.append(frame);

        $(".detailSearch").click(function (e){
            e.preventDefault();
            $(".getMovies").empty();
            console.log("Advanced Search button clicked");
            console.log("SessionID: "+sessionID);
            console.log("Email: "+email);
            title = $(".advancedTitle").val();
            director = $(".director").val();
            year = $(".year").val();
            genre = $(".genre").val();
            globalLimit = $(".limit").val();
            // globalOffset = ($(".offset").val()-1) * globalLimit;
            globalOffset = $(".offset").val();
            orderby = $(".orderby").val();
            direction = $(".direction").val();
            console.log("USer input globalOffset: "+globalOffset);

            if (!globalOffset || globalOffset < 0) globalOffset = 0;
            console.log("Modified globalOffset: "+globalOffset);
            if (!orderby) orderby = "rating";
            if (!globalLimit) globalLimit = 10;
            if (!direction) direction = "desc";

            let urlBuilder = "http://127.0.0.1:2328/api/g/movies/search?title="+title+
                "&director="+director+"&year="+year+"&genre="+genre+
                "&offset="+globalOffset+"&limit="+globalLimit+"&orderby="+orderby+
                "&direction="+direction;

            urlBuilder = urlBuilder.replace(" ", "%20");

            console.log("URL: "+urlBuilder);
            $.ajax({
                url: urlBuilder,
                method: "GET",
                headers: {
                    "Access-Control-Expose-Headers": "*",
                    "Access-Control-Allow-Origin": "*",
                    "email": email,
                    "sessionID": sessionID
                },
                crossDomain: true,
                success: function(result, textstatus, xhr){
                    if (xhr.status == 204)
                        handleMovieNoContent(result, textstatus, xhr);
                    else if (xhr.status == 200)
                        handleMovieSuccess;
                    else
                        handleError;
                },
                statusCode: {
                    400: handleError,
                    500: handleError
                }
            })
        })
    });

    $(".clearCart").click(function (e) {
        e.preventDefault();
        if (!sessionID){
            console.log("SessionID not provided.");
            $("#regLogUser").trigger('click');
        }
        globalOffset = 0;
        globalLimit = 10;

        let userEmail = email;
        let requestModel = {email: userEmail};

        $.ajax({
            url: "http://127.0.0.1:2328/api/g/billing/cart/clear",
            method: "POST",
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*",
                "email": email,
                "sessionID": sessionID
            },
            contentType: "application/json",
            crossDomain: true,
            data: JSON.stringify(requestModel),
            success: handleNoContent,
            statusCode: {
                400: handleError,
                500: handleError
            }
        })
    });

    $(".checkOut").click(function (e) {
        e.preventDefault();
        console.log("Checkout button clicked...");
        if (!sessionID){
            console.log("SessionID not provided.");
            $("#regLogUser").trigger('click');
        }
        globalLimit = 10;
        globalOffset = 0;

        let userEmail = email;
        let fName = "Donald";
        let lName = "Trump";
        let creditCard = "0092471023921436006";
        let theAddress = "heaven";

        let customerRequestModel = {
            email: userEmail,
            firstName: fName,
            lastName: lName,
            ccId: creditCard,
            address: theAddress
        };

        console.log("Created customer insert request model");
        $.ajax({
            url: "http://127.0.0.1:2328/api/g/billing/customer/insert",
            contentType: "application/json",
            method: "POST",
            crossDomain: true,
            data: JSON.stringify(customerRequestModel),
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*",
                "email": email,
                "sessionID": sessionID
            },
            success: handleNoContent,
            statusCode: {
                400: handleError,
                500: handleError
            }
        });
    });

    function realCheckOut(){
        let userEmail = email;
        let requestModel = {email: userEmail};
        setTimeout(function(){
            $('.showCart').trigger('click');
        }, 2000);
        $.ajax({
            url: "http://127.0.0.1:2328/api/g/billing/order/place",
            contentType: "application/json",
            data: JSON.stringify(requestModel),
            method: "POST",
            crossDomain: true,
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*",
                "email": email,
                "sessionID": sessionID
            },
            success: handleNoContent,
            statusCode: {
                400: handleError,
                500: handleError
            }
        });
    }

    $("#userOrderHistory").click(function (e) {
        e.preventDefault();
        //if session = null;
        console.log("Order History clicked");
        if (!sessionID){
            console.log("SessionID not provided.");
            setTimeout(function(){
                $("#regLogUser").trigger('click');
            }, 500);
        }
        globalLimit = 10;
        globalOffset = 0;

        let retrieveRequestModel = {email: email};

        $.ajax({
            url: "http://127.0.0.1:2328/api/g/billing/order/retrieve",
            contentType: "application/json",
            data: JSON.stringify(retrieveRequestModel),
            method: "POST",
            crossDomain: true,
            headers: {
                "Access-Control-Expose-Headers": "*",
                "Access-Control-Allow-Origin": "*",
                "email": email,
                "sessionID": sessionID
            },
            success: handleNoContent,
            statusCode: {
                400: handleError,
                500: handleError
            }
        })

    })
});

