Game.controller('DeseaseController', [
    '$scope',
    'Diseases',
    function($scope, Diseases) {

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
        $scope.hasDecease = false;
        var luck = $scope.$root.day.luck;
        var diseases = Diseases.get();
        $scope.deceases = [];

        if (diseases) {

          if (diseases.length != 0) {
            $scope.hasDecease = true;
            for (var i = 0; i < diseases.length; i++) {
              var d = diseases[i];
              $scope.deceases.push({
                name: d,
                url: '/public/images/diseases/' + d + '.png'
              });
            }
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
