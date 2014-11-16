Game.controller('GrowingStateController', [
  '$scope',
  'Operations',
  '$farmer',
  '$weather',
  '$window',
  '$day',
  '$plantation',
  'Diseases',
  function($scope, Operations, $farmer, $weather, $window, $day, $plantation, Diseases) {

    $scope.nextDay = true;

    $scope.showShop = function() {
      $scope.$root.$emit('operation-store', {
        ico: "/public/images/game/operations/shop.png",
        name: 'store',
        price: 0,
        duration: 0,
        order: 1
      });
    }

    $scope.onResizeFunction = function() {
      $scope.atom = $window.innerWidth * 0.8 / 20;
    };

    // Call to the function when the page is first loaded
    $scope.onResizeFunction();

    angular.element($window).bind('resize', function() {
      $scope.onResizeFunction();
      $scope.$apply();
    });

    $farmer.load();
    $weather.load();
    Diseases.load();

    $scope.$root.$watch('day.season_level', function(n) {
      if (!n) return;
      $scope.actions = Operations['season-' + n];
      $scope.season = n;
    })

    $plantation.load(function() {
      $scope.coords = JSON.parse($scope.$root.plantation.treePositions);
      $scope.seedling = $scope.coords.length;
      $scope.totalSeedling = $scope.coords.length;

      var isInCoords = function(p) {
        var arr = $scope.coords;

        for (var i = 0; i < arr.length; i++) {
          var el = arr[i];
          if (el.x === p.x && el.y === p.y) {
            p.color = el.color;
            return true;
          }
        }
        return false;
      }

      $scope.rows = [];
      // var N = 15, M = 5;
      var N = 15,
        M = 5;
      var r = {
        cols: []
      };
      $scope.rows.push(r);
      for (var i = 0; i < N; i++) {
        r.cols.push({
          show: true,
          staticV: true,
          x: i,
          y: j,
          terrain: '/public/images/ograda.png',
          cls: 'ograda-gore'
        })
      }

      for (var j = 0; j < M; j++) {
        var r = {
          cols: []
        };
        $scope.rows.push(r);
        for (var i = 0; i < N; i++) {
          $scope.totalSeedling++;
          r.cols.push({
            show: true,
            active: true,
            staticV: false,
            x: i,
            y: j,

            terrain: $scope.$root.farmer.soil_url,
            cls: 'pocva'
          })

        }
        for (var i = 0; i < N; i++) {
          var p = r.cols[i];
          var color = "";
          if (p.active && isInCoords(p)) {
            //r.cols[i].tree = $scope.$root.farmer.plant_url;
            r.cols[i].tree = p.color;
            r.cols[i].treeCls = 'seedling clickable';
          }
        }

      }
    });


    $scope.checkTreeType = function(p) {
      var result = "";
      if (p === "red") {
        result = $scope.$root.farmer.plant_url;
      } else if (p === "green") {
        result = $scope.$root.farmer.plant_url_green;
      } else if (p === "gold") {
        result = $scope.$root.farmer.plant_url_gold;
      }
      return result;
    }
    $scope.treeClick = function(p) {
      $scope.$root.$emit('operation-desease-analysis');
    };

  }
]);
