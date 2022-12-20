import logo from './logo.svg';
import './App.css';
// import App from "./App";
import ReactDOM from 'react-dom';
import React from 'react';
import {
    BrowserRouter, Redirect,
    Route, Routes, Navigate
} from "react-router-dom";
import ResultXlsxlToJson from "./components/Pages/ResultXlsxToJson/ResultXlsxlToJson";
import Edit from "./components/Pages/Edit/EditXlsxToXlsx";
import UploadXlsxToXlsx from "./components/Pages/UploadXlsxToXlsx/UploadXlsxToXlsx";
import UploadXlsxToJson from "./components/Pages/UploadXlsxToJson/UploadXlsxToJson";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Main from "./components/Pages/Main/Main";


function App() {
    return (
        <BrowserRouter>
            <div className='app-wrapper'>
                <Header/>
                <Routes>
                    <Route path='/' element={<Main/>}/>
                    <Route path='/uploadXlsxToXlsx' element={<UploadXlsxToXlsx/>}/>
                    <Route path='/uploadXlsxToJson' element={<UploadXlsxToJson/>}/>
                    <Route path='/edit' element={<Edit/>}/>
                    {/*<Route path='/result' element={<ResultXlsxlToJson/>}/>*/}
                </Routes>
                <Footer/>
            </div>
        </BrowserRouter>
    );
}

export default App;
