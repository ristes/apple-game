Game.directive('pruneGame', ['$interval', function($interval) {
  return {
    restrict: 'E',
    scope: {
      togglePoint: '&',
      year: '=',
      gameOver: '=',
      gWidth: '@',
      gHeight: '@'
    },
    templateUrl: '/public/_views/prune_game.html',

    link: function(scope, element, attrs){

      scope.render = function(){
        element.find('.game-viewport').empty();
        var svg = d3.select('.game-viewport')
          .attr("width", 400)
          .attr("height", 400)
          .style("background", "url(/public/images/mini-games/pruning-year-" + scope.year + "-small.png)")
          .on('click', function(){
            // console.log("X: " + d3.event.offsetX);
            // console.log("Y: " + d3.event.offsetY);
            // console.log("==============");
          })

        svg.selectAll('.cut-point')
          .data(scope.cutPoints)
          .enter()
          .append('circle')
            .classed('cut-point', true)
            .classed('cut', function(d){return d.clicked;})
            .attr('r', 10)
            .attr('cx', function(d){ return d.x})
            .attr('cy', function(d){ return d.y})
            .on('click', function(d){
              d.clicked = !d.clicked;
              // console.log(d.type, d.x, d.y);
              scope.render();
            })

      }

      scope.finnishGame = function(){
        var totalBranches = scope.cutPoints.length;
        var correctCuts = 0;

        scope.cutPoints.forEach(function(p){
          if (p.type === 'cut'){
            if (p.clicked){
              correctCuts++
            }
          } else if (p.type === 'no-cut'){
            if (!p.clicked){
              correctCuts++;
            }
          }
        })
        scope.gameOver({total: totalBranches, correct: correctCuts});
      }

      scope.startGame = function(){
        if (scope.year == "1"){
          scope.cutPoints = [
            {x: 170, y: 212, type: 'no-cut', clicked: false},
            {x: 159, y: 136, type: 'cut', clicked: false},
            {x: 171, y: 113, type: 'no-cut', clicked: false},
            {x: 196, y: 149, type: 'cut', clicked: false},
            {x: 208, y: 225, type: 'cut', clicked: false},
            {x: 203, y: 264, type: 'no-cut', clicked: false},
          ]
        } else {
          scope.cutPoints = [
            {x: 165, y: 299, type: 'no-cut', clicked: false},
            {x: 121, y: 303, type: 'no-cut', clicked: false},
            {x: 140, y: 218 , type: 'no-cut', clicked: false},
            {x: 160, y: 207 , type: 'no-cut', clicked: false},
            {x: 173, y: 179 , type: 'no-cut', clicked: false},
            {x: 171, y: 141 , type: 'no-cut', clicked: false},
            {x: 136, y: 150 , type: 'no-cut', clicked: false},
            {x: 178, y: 95 , type: 'no-cut', clicked: false},
            {x: 180, y: 50 , type: 'no-cut', clicked: false},
            {x: 196, y: 47 , type: 'no-cut', clicked: false},
            {x: 289, y: 171 , type: 'no-cut', clicked: false},
            {x: 322, y: 158 , type: 'no-cut', clicked: false},
            {x: 199, y: 212 , type: 'no-cut', clicked: false},
            {x: 194, y: 240 , type: 'no-cut', clicked: false},
            {x: 237, y: 243 , type: 'no-cut', clicked: false},
            {x: 272, y: 226 , type: 'no-cut', clicked: false},

          ]
        }

        scope.render();
      }

      scope.startGame();

      scope.$watch('cutPoints', function(){
        scope.render();
      }, true);
    }
  };
}]);