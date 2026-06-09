import React, { Suspense } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import * as serviceWorker from "./serviceWorker";
import reportWebVitals from "./reportWebVitals";
import { HashRouter } from "react-router-dom";
//theme-layout
import ThemedSuspense from "./layout/ThemedSuspense";
//store
import { Provider } from "react-redux";
import store from "./store";
const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <Suspense fallback={<ThemedSuspense />}>
        <HashRouter fallback={<ThemedSuspense />}>
          <App />
        </HashRouter>
      </Suspense>
    </Provider>
  </React.StrictMode>
);

reportWebVitals();
// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
