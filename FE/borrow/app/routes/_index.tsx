import { useEffect, useState } from "react";
import axios, { AxiosResponse } from "axios";

interface Item {
    name: string;
    borrowee: string;
}

interface Borrowee {
    name: string;
}

interface Data {
    items: Item[];
    borrowees: Borrowee[];
}

export default function Index() {
    const [data, setData] = useState<Data>({ items: [], borrowees: [] });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const loadedData = await loader();
            setData(loadedData);
            console.log(loadedData);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    return (
        <div>
            <h2>Items</h2>
            <ul>
                {data.items.map((item, index) => (
                    <li key={index}>{item.name}</li>
                ))}
            </ul>

            <h2>Borrowees</h2>
            <ul>
                {data.borrowees.map((borrowee, index) => (
                    <li key={index}>{borrowee.name}</li>
                ))}
            </ul>
        </div>
    );
}

export async function loader(): Promise<Data> {
    const response: AxiosResponse = await axios.get('http://localhost:8080');

    const itemsResponse: AxiosResponse<{ data: Item[] }> = await axios.get(response.data._links.items.href);
    const borroweesResponse: AxiosResponse<{ data: Item[] }> = await axios.get(response.data._links.borrowees.href);

    const items: Item[] = itemsResponse.data.data;
    const borrowees: Item[] = borroweesResponse.data.data;

    return {
        items,
        borrowees
    };
}
