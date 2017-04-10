const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: './index.js',

    output: {
        path: path.join(__dirname, 'generated'),
        filename: 'app-bundle.js'
    },

    devtool: 'source-map',
    resolve: {extensions: ['.js', '.jsx']},
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/
            },
            {
                test: /\.less$/,
                use: [
                    'style-loader',
                    {loader: 'css-loader', options: {importLoaders: 1}},
                    'less-loader'
                ]
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(png|jpg|gif)$/,
                loader: "file-loader?name=img/img-[hash:6].[ext]"
            }
        ]
    },

    devServer: {
        contentBase: './app',
        inline: true,
        historyApiFallback: true,
    }
};