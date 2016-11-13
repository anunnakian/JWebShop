(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-line', {
            parent: 'entity',
            url: '/order-line',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jWebShopApp.orderLine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-line/order-lines.html',
                    controller: 'OrderLineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderLine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('order-line-detail', {
            parent: 'entity',
            url: '/order-line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jWebShopApp.orderLine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-line/order-line-detail.html',
                    controller: 'OrderLineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderLine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OrderLine', function($stateParams, OrderLine) {
                    return OrderLine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'order-line',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('order-line-detail.edit', {
            parent: 'order-line-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-line/order-line-dialog.html',
                    controller: 'OrderLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderLine', function(OrderLine) {
                            return OrderLine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-line.new', {
            parent: 'order-line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-line/order-line-dialog.html',
                    controller: 'OrderLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                qty: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-line', null, { reload: 'order-line' });
                }, function() {
                    $state.go('order-line');
                });
            }]
        })
        .state('order-line.edit', {
            parent: 'order-line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-line/order-line-dialog.html',
                    controller: 'OrderLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderLine', function(OrderLine) {
                            return OrderLine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-line', null, { reload: 'order-line' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-line.delete', {
            parent: 'order-line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-line/order-line-delete-dialog.html',
                    controller: 'OrderLineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderLine', function(OrderLine) {
                            return OrderLine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-line', null, { reload: 'order-line' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
