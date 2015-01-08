Game.directive('harvestGame', ['$interval', function($interval) {
  return {
    restrict: 'E',
    scope: {
      game: '=',
      apples: '=',
      basketPosition: '='
    },
    templateUrl: '/public/_views/harvest_game.html',

    link: function($scope, element, attrs){
      var width = 1000;
      var height = 500;
      var padding = 50;
      var basketHeight = 20;
      var svg = d3.select('.game-viewport')
        .attr("width", width)
        .attr("height", height)

      // scales
      var yScale = d3.scale.linear()
        .range([padding, height-padding])
        .domain([0, 100]);
      var xScale = d3.scale.linear()
        .range([padding, width-padding])
        .domain([0, 100]);

      // move basket on mouse move
      svg
        .on("mousemove", function() {
          if (!$scope.basketPosition[0]){
            return;
          }
          var pt = d3.mouse(this);
          position = xScale.invert(pt[0]);
          if (position < 0){
            position = 0;
          }
          if (position > 100){
            position = 100;
          }
          $scope.basketPosition[0].x = position
        });

      // game stats
      var stats = svg.append('g')
        .classed('stats', true)
        .attr("transform", function(d) { return "translate(" + xScale(0) + "," + yScale(0) + ")"; })
      stats.append('text')
        .classed('score-good', true)
        .text('good apples: 0')
      stats.append('text')
        .classed('score-bad', true)
        .attr('y', 20)
        .text('bad apples: 0')
      stats.append('text')
        .classed('timeleft', true)
        .attr('x', xScale(90))
        .text('Time left: 0')
        .attr('text-anchor', 'end')

      $scope.render = function(){
        // apples
        $scope.vis = svg.selectAll(".apple")
          .data($scope.apples, function(d){ return d.id })
          .attr("transform", function(d) { return "translate(" + xScale(d.x) + "," + yScale(d.y) + ")"; })
        $scope.vis
          .enter().append("g")
            .attr("class", "apple")
            .attr("transform", function(d) { return "translate(" + xScale(d.x) + "," + yScale(d.y) + ")"; })
            .append('circle')
              .attr('r', 15)
              .style('fill', function(d){ return d.type == 'good' ? 'green' : 'red'})
        $scope.vis
          .exit().remove();

        var basketPosition = function(d, i){
          var basketPos = xScale(d.x);
          basketPos = basketPos - xScale($scope.game.basketWidth)/2;
          return "translate(" + basketPos + "," + yScale(100) + ")";
        }

        // basket
        $scope.basket = svg.selectAll(".basket")
          .data($scope.basketPosition)
          .attr("transform", basketPosition)

        $scope.basket.enter().append("g")
          .attr("class", "basket")
          .attr("transform", basketPosition)
          .append("rect")
            .attr("width", xScale($scope.game.basketWidth))
            .attr("height", basketHeight)
            .style("fill", "blue")
        $scope.basket.exit().remove()

        // update game stats indicators
        svg.select('.score-good')
          .text("Good apples: " + $scope.game.goodCaught)
        svg.select('.score-bad')
          .text("Bad apples: " + $scope.game.badCaught)
        svg.select('.timeleft')
          .text("Time left: " + $scope.game.timeLeft)
      }

      tick = $interval(function() {
        $scope.render();
      }, 1000/60); // 60 FPS
    }
  };
}]);