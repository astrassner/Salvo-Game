var battlefield = new Vue({
    el: ".Grid",
    data: {
        url: "http://localhost:8080/api/game_view/",
        gridLetters: ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        gridNumbers: ["", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        data: [],
        gamePlayerId: "",
        currentPlayer: "",
        enemyPlayer: "",
        currentPlayerId: 0,

    },
    created: function () {
        /*this.buildGrid();*/
        this.getData();
    },
    methods: {
        /*buildGrid: function () {
            for (let i = 0; i < 11; i++) {
                //create divs wich contains the single divs for one row
                let gridRow = $("<div class=" + "gridRow" + "></div>");
                $("#playerGrid").append(gridRow);
                for (let k = 0; k < 11; k++) {
                    //create the single divs per row
                    let gridCells = $("<div class=" + "gridCells" + " " + "id=" + (this.gridLetters[i] + this.gridNumbers[k]) + ">" + (this.gridLetters[i] + this.gridNumbers[k]) + "</div>");

                    gridRow.append(gridCells);

                }
            }
        },*/
        getUrlId: function () {
            var obj = {};
            var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
            var search = window.location.href;

            search.replace(reg, function (match, param, val) {
                obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
            });
            
            this.gamePlayerId = obj.gp;

            return obj.gp;
        },
        getData: function () {
            fetch(this.url + this.getUrlId())
                .then(function (r) {
                    return r.json().then(function (data) {
                        battlefield.data = data;
                        battlefield.addShips();
                        battlefield.gameInfo();
                        battlefield.addSalvos();
                    })
                });

        },
        addShips: function(){
            
            let ships = this.data.ships;
            
            for(let i = 0; i < ships.length; i++){
                /*let ship = ships[i];*/
                for(let k = 0; k < ships[i].locations.length; k++){
                    let locations = ships[i].locations[k];
                    /*console.log(locations);*/
                    
                    $("#" + locations).addClass("ship");
                      
                } 
            } 
        },
        gameInfo: function(){
            let gamePlayer = this.data.gamePlayers;
            for(let i = 0; i < gamePlayer.length; i++){
                if(gamePlayer[i].id == this.gamePlayerId){
                    this.currentPlayer = gamePlayer[i].player["email"];
                    this.currentPlayerId = gamePlayer[i].player["id"];
                        
                }else{
                    this.enemyPlayer = gamePlayer[i].player["email"];
                }
            }
        },
        addSalvos: function(){
            
            let salvos = this.data.salvos;
            
            for(let i = 0; i < salvos.length; i++){
                /*console.log(salvos[i]);*/
                for(let k = 0; k < salvos[i].locations.length; k++){
                    if(salvos[i].playerId == this.currentPlayerId){
                        console.log(salvos[i].locations[k]);
                        var locations = salvos[i].locations[k];
                        $('[data-cell= "' + locations + '"]').addClass("salvoPlayer");
                    }else{
                        var locations = salvos[i].locations[k];
                        if($("#" + locations).hasClass("ship")){
                            $("#" + locations).addClass("hit");
                        }else{
                            $("#" + locations).addClass("salvoPlayer");
                        }
                    }
                    
                }
                
            }
        }
        
    }
})

/*function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });

    console.log(obj);

    return obj.gp;
}
let url = window.location.href;

paramObj(url);*/

