// CustomPagination.tsx
import * as React from 'react';
import Pagination from '@mui/material/Pagination';

interface CustomPaginationProps {
    onPageChange: (pageNumber: number, pageSize: number) => void;
}

function CustomPagination({ onPageChange }: CustomPaginationProps) {
    const [page, setPage] = React.useState(1);
    const [pageSize, setPageSize] = React.useState(10);
    const [totalPages, setTotalPages] = React.useState(1);

    const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setPage(value);
        onPageChange(value, pageSize);
    };

    const handlePageSizeChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        const newSize = Number(event.target.value);
        setPageSize(newSize);
        setPage(1);
        onPageChange(1, newSize);
    };

    return (
        <div>
            <p>Page: {page}</p>
            <label htmlFor="pageSize">Items per page: </label>
            <select id="pageSize" value={pageSize} onChange={handlePageSizeChange}>
                <option value={1}>1</option>
                <option value={5}>5</option>
                <option value={10}>10</option>
            </select>
            <Pagination count={totalPages} page={page} onChange={handleChange} />
        </div>
    );
}

export default CustomPagination;



// import * as React from 'react';
// import Pagination from '@mui/material/Pagination';
// import axios from 'axios';
//
// interface CustomPaginationProps {
//     status: 'completed' | 'in_progress' | 'cancelled' | null;
// }
//
// function CustomPagination({ status }: CustomPaginationProps) {
//     const [page, setPage] = React.useState(1);
//     const [pageSize, setPageSize] = React.useState(1);
//     const [totalPages, setTotalPages] = React.useState(1);
//
//     const fetchData = async (pageNumber: number, size: number) => {
//         try {
//             const allOrdersWithStatus = await axios.get(`http://localhost:8080/api/v1/corso/orders?status=${status}&pageSize=0&offset=0`);
//             const totalOrdersWithStatus = allOrdersWithStatus.data.length;
//
//             const response = await axios.get(`http://localhost:8080/api/v1/corso/orders?status=${status}&pageSize=${size}&offset=${(pageNumber - 1) * size}`);
//             console.log(response.data);
//             const newTotalPages = Math.ceil(totalOrdersWithStatus / size);
//
//             setTotalPages(newTotalPages);
//         } catch (error) {
//             console.error('Error fetching data:', error);
//         }
//     };
//
//     const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
//         setPage(value);
//         fetchData(value, pageSize);
//     };
//
//     const handlePageSizeChange = (event: React.ChangeEvent<{ value: unknown }>) => {
//         const newSize = Number(event.target.value);
//         setPageSize(newSize);
//         setPage(1);
//         fetchData(1, newSize);
//     };
//
//     return (
//         <div>
//             <p>Page: {page}</p>
//
//             {/* Dropdown for selecting page size */}
//             <label htmlFor="pageSize">Items per page: </label>
//             <select id="pageSize" value={pageSize} onChange={handlePageSizeChange}>
//                 <option value={1}>1</option>
//                 <option value={5}>5</option>
//                 <option value={10}>10</option>
//                 {/* Add more options based on your requirements */}
//             </select>
//
//             <Pagination count={totalPages} page={page} onChange={handleChange} />
//         </div>
//     );
// }
//
// export default CustomPagination;