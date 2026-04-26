import "./App.css";
import { Route, Routes } from "react-router-dom";
import Home from "./modules/core/pages/HomePage";
import Layout from "./modules/core/Layout";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Home />} />
      </Route>
    </Routes>
  );
}
