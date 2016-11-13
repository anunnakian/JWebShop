(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('OrderLineController', OrderLineController);

    OrderLineController.$inject = ['$scope', '$state', 'OrderLine'];

    function OrderLineController ($scope, $state, OrderLine) {
        var vm = this;
        
        vm.orderLines = [];

        loadAll();

        function loadAll() {
            OrderLine.query(function(result) {
                vm.orderLines = result;
            });
        }
    }
})();
