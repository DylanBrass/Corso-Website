import * as React from 'react';
import Pagination from '@mui/material/Pagination';

export {};

interface CustomPaginationProps {
    onPageChange: (pageNumber: number, pageSize: number) => void;
    pageSize: number;
    totalPages: number;
}

function CustomPagination({ onPageChange, pageSize, totalPages }: CustomPaginationProps) {
    const [page, setPage] = React.useState(1);
    const [localPageSize, setLocalPageSize] = React.useState(pageSize);

    const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setPage(value);
        onPageChange(value, localPageSize);
    };

    const handlePageSizeChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        const newSize = Number(event.target.value);
        setLocalPageSize(newSize);
        setPage(1);
        onPageChange(1, newSize);
    };

    React.useEffect(() => {
        // Update localPageSize when the pageSize prop changes
        setLocalPageSize(pageSize);
    }, [pageSize]);

    return (
        <div>
            <div style={{ flex: '1', textAlign: 'center' }}>
                <p style={{ fontWeight: 'bold' }}>Page: {page}</p>
            </div>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <label htmlFor="pageSize" style={{ marginRight: '10px' }}>Items per page: </label>
                <select id="pageSize" value={localPageSize} onChange={handlePageSizeChange}>
                    <option value={1}>1</option>
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                </select>
                <Pagination count={totalPages} page={page} onChange={handleChange} style={{ marginLeft: '10px' }} />
            </div>
        </div>

    );
}

export default CustomPagination;

