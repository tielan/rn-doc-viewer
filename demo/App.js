/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  SafeAreaView, Button,
} from 'react-native';
import DocViewer from 'rn-doc-viewer'

const App = () => {
  return (
    <>
      <SafeAreaView>
        <Button
          title="打开pdf"
          onPress={() => {
            DocViewer.openDoc({ url: 'http://appapidemo.egp.c2cloud.cn/a.pdf' }, () => {

            })
          }}></Button>
        <Button
          title="打开图片"
          onPress={() => {
            DocViewer.openImg([{ url: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2567670815,24101428&fm=26&gp=0.jpg' }], () => {

            })
          }}></Button>
        <Button
          title="打开video"
          onPress={() => {
            DocViewer.openVideo({ url: 'http://172.16.17.64:8088/bg.mp4' }, () => {

            })
          }}></Button>
      </SafeAreaView>
    </>
  );
};

export default App;
