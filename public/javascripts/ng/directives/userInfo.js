'use strict';

GameDirectives.directive('userInfo', [
    'jQuery',
    'State',
    function($, State) {

        return {
            restrict: 'E',
            transclude: true,
            scope: {},
            link: function(scope, element, attrs, ctrl, transclude, formCtrl) {

                $.cssEase['bounce'] = 'cubic-bezier(0,1,0.5,1.3)';
                $(element).find("#div_user_icon").transition({
                    left: '15px'
                }, 1000, 'ease');
                $(element).find("#div_apple_in_stock_info").transition({
                    top: '-10px'
                }, 1000, 'bounce');
                $(element).find("#div_money_in_stock_info").transition({
                    top: '-10px'
                }, 1000, 'bounce');
                $(element).find("#div_eco_points_info").transition({
                    top: '-10px'
                }, 1000, 'bounce');

            },
            controller: function($scope) {
                $scope.farmer = State.getByField("farmer");

                State.subscribe("farmer", "infoPanelFarmerSubscription", function(farmer) {
                    $scope.gameState = farmer;
                });

                $scope.sell = function() {
                    $scope.$root.$emit('operation-sell');
                };

            },
            templateUrl: '/public/templates/user-info.html'
        };

    }
]);
