/*$.ajax({
  url: "http://localhost:8080/api/games",
  data: JSON,
  dataType: "json"
  
}).done(function(data){
    
    console.log(data);
    
    let body = document.getElementById("gameList");
    
    for (let i = 0; i < data.length; i++){
        
        let li = document.createElement("li");
        
        let game = data[i].created;
        
        li.append(game);
        
        let gamePlayerLength = data[i].gamePlayers.length;
        
        for(let k = 0; k < gamePlayerLength; k++){
            
            let player = ", " + data[i].gamePlayers[k].player.email;
            
            li.append(player);
                
            console.log(data[i].gamePlayers[k].player.email);
            
        }
        
        body.append(li);
        
        
    }
    
    
});*/

var games = new Vue({
    el: ".ListGames",
    data: {
        url: "http://localhost:8080/api/games",
        data: [],
        currentUser: "",

    },
    created: function () {
        this.getData();
    },
    methods: {
        getData: function () {
            fetch(this.url)
                .then(function (r) {
                    return r.json().then(function (data) {
                        games.data = data;
                        games.getCurrentUser();
                    })
                });
            //            this.$http.get(this.url).then(response => {
            //
            //                this.data = response.body;
            //                console.log(this.data);
            //            })
        },
        login: function () {

            var username = $("#username").val();
            var password = $("#password").val();

            $.post("/api/login", {
                    username: username,
                    password: password
                })
                .done(function () {

                    location.reload()
                })
                .fail();
        },
        logout: function (evt) {
            evt.preventDefault();

            $.post("/api/logout")
                .done(function () {

                    location.reload()
                })
                .fail()
        },
        signUp: function () {

            var username = $("#username").val();
            var password = $("#password").val();

            $.ajax({
                type: "POST",
                url: "/api/players",
                data: "userName=" + username + "&password=" + password
            }).fail(function(error){
                console.log(error);
            }).done(function(response){
                $("#username").val('');
                $("#password").val('');
                console.log(response);
            })

        },
        getCurrentUser: function () {

            if (this.data.player !== undefined) {
                this.currentUser = this.data.player.name;
            } else {
                this.currentUser = "Guest";
            }
        }

    }
})

var leaderboard = new Vue({
    el: ".leaderBoard",
    data: {
        url: "http://localhost:8080/api/leader_board",
        data: [],
        board: [],

    },
    created: function () {
        this.getData();
    },
    methods: {
        getData: function () {
            fetch(this.url)
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    leaderboard.data = data;

                    leaderboard.comparator(data);

                })
        },
        comparator: function (data) {
            var finalArray = [];

            for (let player in data) {
                var everyPlayer = {};
                everyPlayer.name = player;

                for (let prop in data[player]) {
                    everyPlayer[prop] = data[player][prop];
                }

                finalArray.push(everyPlayer);
            }

            finalArray.sort(function (a, b) {
                return b.total - a.total;
            })

            this.board = finalArray;

        },
    }

})
