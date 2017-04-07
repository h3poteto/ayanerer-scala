const path = require('path')
const ExtractTextPlugin = require('extract-text-webpack-plugin')
const CopyWebpackPlugin = require("copy-webpack-plugin");

module.exports = {
  entry: {
    vendor: ['babel-polyfill'],
    'stylesheets/main.css': './app/frontend/stylesheets/main.scss',
    'javascripts/main.js': './app/frontend/javascripts/main.js'
  },
  output: {
    path: path.resolve(__dirname, 'public'),
    filename: '[name]'
  },
  devtool: "source-map",
  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: "babel-loader",
        query: {
          presets: ["es2015"]
        }
      },
      {
        test: /\.(scss|css)$/,
        loader: ExtractTextPlugin.extract({ fallback: 'style-loader', use: 'css-loader!sass-loader' }),
      },
      {
        test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader?limit=10000&mimetype=application/font-woff"
      },
      {
        test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader?limit=10000&mimetype=application/octet-stream"
      },
      {
        test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
        loader: "file-loader"
      },
      {
        test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader?limit=10000&mimetype=image/svg+xml"
      }
    ]
  },
  plugins: [
    new ExtractTextPlugin("[name]"),
    new CopyWebpackPlugin([{ from: "./app/frontend/images", to: "images/" }])
  ],
  resolve: {
    extensions: ['*', '.css', '.scss', '.js'],
    modules: [path.resolve(__dirname, 'app/frontend'), "node_modules"],
  },
}
