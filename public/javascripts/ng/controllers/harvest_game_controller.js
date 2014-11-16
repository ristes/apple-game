app.controller('HarvestGameController', ['$scope', '$interval', function($scope, $interval) {

  $scope.appleId = 1;
  $scope.apples = [];
  $scope.maxSpeedThreshold = 1;
  $scope.basketPosition = [{x: 50}];
  $scope.game = {
    score: 0,
    totalGood: 0,
    totalBad: 0,
    goodCaught: 0,
    badCaught: 0,
    basketWidth: 6,
    timeLeft: 60,
    dropAppleInterval: 60
  }

  $scope.ticks = 0;
  $scope.tick = $interval(function() {
    // update timer
    if ($scope.ticks % 60 == 0){
      $scope.game.timeLeft--;
      if ($scope.game.timeLeft <= 0){
        $scope.gameOver();
        return;
      }

      $scope.game.dropAppleInterval = $scope.game.timeLeft + 10;
    }
    // update apple positions
    $scope.apples.forEach(function(apple, i){
      if (apple){
        apple.y += apple.v;
        if (apple.y >= 100){
          // check if apple was caught
          if ((apple.x > $scope.basketPosition[0].x-$scope.game.basketWidth+2/2) && (apple.x < $scope.basketPosition[0].x + $scope.game.basketWidth+2/2) ){
            // update game stats
            if (apple.type == 'good'){
              $scope.game.goodCaught++;
            } else {
              $scope.game.badCaught++;
            }
          }
          $scope.apples.splice(i,1);
        }
      }
    });

    // add an apple every x frames
    if ($scope.ticks % $scope.game.dropAppleInterval == 0){
      var type = (Math.random() > 0.3) ? 'good' : 'bad';
      var apple = {
        id: $scope.appleId++,
        type: type,
        x: Math.round(Math.random()*100),
        y: 0,
        v: (Math.random() * $scope.maxSpeedThreshold) + 0.7
      }
      $scope.apples.push(apple);

      // update game stats
      if (apple.type == 'good'){
        $scope.game.totalGood++;
      } else {
        $scope.game.totalBad++;
      }
    }
    $scope.ticks++;
  }, 1000/60); // 60 FPS

  $scope.gameOver = function(){
    $scope.apples = [];
    $scope.basketPosition = [];
    $scope.appleId = 1;
    $interval.cancel($scope.tick)

    $scope.sendStatsToServer($scope.game)
    console.log($scope.game)
  }

  $scope.sendStatsToServer(stats){
    alert('Implement me in harvest_game_controller.js: sendStatsToServer(stats)')
  }

}]);