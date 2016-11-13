(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('OrderLineDetailController', OrderLineDetailController);

    OrderLineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrderLine', 'OrderObject', 'Product'];

    function OrderLineDetailController($scope, $rootScope, $stateParams, previousState, entity, OrderLine, OrderObject, Product) {
        var vm = this;

        vm.orderLine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jWebShopApp:orderLineUpdate', function(event, result) {
            vm.orderLine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
