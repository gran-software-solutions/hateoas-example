import React, { useState, useEffect } from 'react';
import axios from 'axios';

const App = () => {
    const [data, setData] = useState(null);
    const [links, setLinks] = useState({});

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await axios.get('http://localhost:8080');
                setData(response.data);
                setLinks(parseLinks(response.data._links));
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        }
        fetchData();
    }, []);
};

