Game.directive('harvestGame', ['$interval', function($interval) {
    return {
        restrict: 'E',
        scope: {
            game: '=',
            apples: '=',
            gameOver: '=',
            gWidth: '@',
            gHeight: '@'
        },
        templateUrl: '/public/_views/harvest_game.html',

        link: function($scope, element, attrs) {
            $scope.basketPosition = [{
                x: 50
            }];
            var width = $scope.gWidth || 400;
            var height = $scope.gHeight || 500;
            var padding = 50;
            var basketHeight = 20;
            var svg = d3.select('.game-viewport')
                .attr("width", width)
                .attr("height", height);
            $scope.hit = "";

            // scales
            var yScale = d3.scale.linear()
                .range([padding, height - padding])
                .domain([0, 100]);
            var xScale = d3.scale.linear()
                .range([padding, width - padding])
                .domain([0, 100]);

            // move basket on mouse move
            svg
                .on("mousemove", function() {
                    if (!$scope.basketPosition[0]) {
                        return;
                    }
                    var pt = d3.mouse(this);
                    position = xScale.invert(pt[0]);
                    if (position < 0) {
                        position = 0;
                    }
                    if (position > 100) {
                        position = 100;
                    }
                    $scope.basketPosition[0].x = position
                });

            // game stats
            var stats = svg.append('g')
                .classed('stats', true)
                .attr("transform", function(d) {
                    return "translate(" + xScale(0) + "," + yScale(0) + ")";
                })

            $scope.appleContainer = svg.append('g');

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

            $scope.render = function() {
                // apples
                $scope.vis = $scope.appleContainer.selectAll(".apple")
                    .data($scope.apples, function(d) {
                        return d.id
                    })
                    .attr("transform", function(d) {
                        return "translate(" + xScale(d.x) + "," + yScale(d.y) + ")";
                    });

                $scope.vis.enter().append("g")
                    .attr("class", "apple")
                    .attr("transform", function(d) {
                        return "translate(" + xScale(d.x) + "," + yScale(d.y) + ")";
                    })
                    .append("image")
                    .attr("xlink:href", function(d) {
                        return '/public/images/mini-games/collect-game-' + d.type + '-apple.png';
                    })
                    .attr('width', 50)
                    .attr('height', 50);

                $scope.vis.exit().remove();

                var basketPosition = function(d, i) {
                    var basketPos = xScale(d.x);
                    // basketPos = basketPos - xScale($scope.game.basketWidth) / 2;
                    return "translate(" + basketPos + "," + (yScale(97) - $scope.game.basketWidth) + ")";
                }

                // basket
                $scope.basket = svg.selectAll(".basket")
                    .data($scope.basketPosition)
                    .attr("transform", basketPosition)

                $scope.basket.enter().append("g")
                    .attr("class", "basket")
                    .attr("transform", basketPosition)
                    .append("image")
                    .attr("xlink:href", '/public/images/mini-games/collect-game-basket' + $scope.hit + '.png')
                    .attr("width", xScale($scope.game.basketWidth))
                    .attr("height", xScale($scope.game.basketWidth))
                $scope.basket.exit().remove()

                // update game stats indicators
                svg.select('.score-good')
                    .text("Good apples: " + $scope.game.goodCaught)
                svg.select('.score-bad')
                    .text("Bad apples: " + $scope.game.badCaught)
                svg.select('.timeleft')
                    .text("Time left: " + $scope.game.timeLeft)
            }; // render

            $scope.tick = function() {
                // update timer
                if ($scope.ticks % 60 == 0) {
                    $scope.game.timeLeft--;
                    if ($scope.game.timeLeft <= 0) {
                        $scope.gameEnd();
                        return;
                    }

                    $scope.game.dropAppleInterval = $scope.game.timeLeft + 10;
                }
                // update apple positions
                $scope.apples.forEach(function(apple, i) {
                    if (apple) {
                        apple.y += apple.v;
                        var hitPoint = yScale(97) - $scope.game.basketWidth;
                        if (yScale(apple.y) >= hitPoint) {
                            // check if apple was caught
                            if ((apple.x > $scope.basketPosition[0].x) && (apple.x < $scope.basketPosition[0].x + $scope.game.basketWidth)) {
                                // update game stats
                                if (apple.type == 'good') {
                                    $scope.game.goodCaught++;
                                } else {
                                    $scope.game.badCaught++;
                                }
                                $scope.hit = "_hit";

                                $scope.apples.splice(i, 1);
                            } else {
                                $scope.hit = "";
                            }
                        }
                    }
                });

                // add an apple every x frames
                if ($scope.game.timeLeft > 5 && $scope.ticks % $scope.game.dropAppleInterval == 0) {
                    var type = (Math.random() > 0.3) ? 'good' : 'bad';
                    var apple = {
                        id: $scope.appleId++,
                        type: type,
                        x: Math.round(Math.random() * 100),
                        y: 0,
                        v: (Math.random() * $scope.maxSpeedThreshold) + 0.7
                    }
                    $scope.apples.push(apple);

                    // update game stats
                    if (apple.type == 'good') {
                        $scope.game.totalGood++;
                    } else {
                        $scope.game.totalBad++;
                    }
                }
                $scope.ticks++;

                $scope.render();
            };

            $scope.gameEnd = function() {
                $interval.cancel($scope.currentGame);
                $scope.apples = [];
                $scope.basketPosition = [];
                $scope.appleId = 1;
                $scope.gameOver($scope.game);

            }

            $scope.startGame = function() {
                $interval.cancel($scope.currentGame);
                
                $scope.appleId = 1;
                $scope.apples = [];
                $scope.maxSpeedThreshold = 1;
                $scope.basketPosition = [{
                    x: 50
                }];
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

                $scope.currentGame = $interval($scope.tick, 1000 / 60);
            }

            $scope.startGame();

        }
    };
}]);
