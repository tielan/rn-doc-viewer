

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
  Button,
} from 'react-native';
import OpenFile from 'rn-doc-viewer';
export default class ReactDocViewerExample extends Component {
  handlePress = () => {
      OpenFile.openDoc([{
        url:"https://www.huf-haus.com/fileadmin/Bilder/Header/ART_3/Header_HUF_Haus_ART_3___1_.jpg", // Local "file://" + filepath
        fileName:"sample.jpg",
        cache:false,
      }], (error, url) => {
         if (error) {
          this.setState({animating: false});
           console.error(error);
         } else {
          this.setState({animating: false});
           console.log(url)
         }
       })
  }
  render() {
    return (
      <View style={styles.container}>
        <Button
          onPress={this.handlePress.bind(this)}
          title="Press Me Open Doc Url"
          accessibilityLabel="See a Document"
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('ReactDocViewerExample', () => ReactDocViewerExample);
