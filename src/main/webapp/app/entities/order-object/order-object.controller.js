(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('OrderObjectController', OrderObjectController);

    OrderObjectController.$inject = ['$scope', '$state', 'OrderObject'];

    function OrderObjectController ($scope, $state, OrderObject) {
        var vm = this;
        
        vm.orderObjects = [];

        loadAll();

        function loadAll() {
            OrderObject.query(function(result) {
                vm.orderObjects = result;
            });
        }
    }
})();
