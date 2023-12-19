if (config.devServer) {
    config.devServer.hot = true;
    config.devServer.historyApiFallback = true;
    config.devServer.port = 3000
    config.devtool = 'eval-cheap-source-map';
} else {
    config.devtool = undefined;
}