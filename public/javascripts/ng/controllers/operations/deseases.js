Game.controller('DeseaseController', [
    '$scope',
    'Diseases',
    'State',
    '$day',
    function($scope, Diseases, State, $day) {

      $scope.visible = false;
      $scope.url = '/public/images/game/operations/desease-analysis.png';

      $scope.hide = function() {
        $scope.visible = false;
      }

      $scope.showDeseaseHelp = function(item) {
        Diseases.getHintAsync(item.name, function(hint) {
          item.help = hint;
        })
      };

      var unreg = $scope.$root.$on('operation-desease-analysis', function(_s,
              oper) {
        $scope.hasProblem = false;
        var luck = $scope.$root.day.luck;
        var diseases = Diseases.get();
        $scope.deceases = [];

        if (diseases) {

          if (diseases.length != 0) {
        	  
            $scope.hasProblem = true;
            for (var i = 0; i < diseases.length; i++) {
              var d = diseases[i];
              $scope.deceases.push({
                name: d,
                url: '/public/images/diseases/' + d + '.png'
              });	
            }
          }
          if ($day.get().needN) {
        	  $scope.hasProblem = true;
        	  $scope.deceases.push({
        		  name:'N',
        		  url: '/public/images/game/nedostatok/N.png'
        	  })
          }
          if ($day.get().needP) {
        	  $scope.hasProblem = true;
        	  $scope.deceases.push({
        		  name: 'P',
        		  url: '/public/images/game/nedostatok/P.png'
        	  })
          }
          if ($day.get().needK) {
        	  $scope.hasProblem = true;
        	  $scope.deceases.push({
        		  name: 'K',
        		  url: '/public/images/game/nedostatok/K.png'
        	  })
          }
        }

        $scope.visible = true;

      });

      var unregHide = $scope.$root.$on('shop-hide', function() {
        $scope.visible = false;
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
        if (unregHide) {
          unregHide();
        }
      });

    }]);
