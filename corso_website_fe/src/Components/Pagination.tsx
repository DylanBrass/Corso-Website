import React, { useState, useEffect } from 'react';
import Pagination from '@mui/material/Pagination';
import {useTranslation} from "react-i18next";

interface CustomPaginationProps {
    onPageChange: (pageNumber: number, pageSize: number) => void;
    pageSize: number;
    totalPages: number;
    resetKey?: string | null;
}

const CustomPagination: React.FC<CustomPaginationProps> = ({
                                                               onPageChange,
                                                               pageSize,
                                                               totalPages,
                                                               resetKey,
                                                           }) => {
    const [page, setPage] = useState(1);
    const [localPageSize, setLocalPageSize] = useState(pageSize);
    const [t] = useTranslation();

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

    useEffect(() => {
        // Update localPageSize when the pageSize prop changes
        setLocalPageSize(pageSize);
        setPage(1); // Reset page to 1 when pageSize changes
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pageSize]);

    useEffect(() => {
        // Reset local state when the resetKey (status) changes
        setPage(1);
        setLocalPageSize(pageSize);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [resetKey]);

    return (
        <div>
            <div style={{ flex: '1', textAlign: 'center' }}>
                <p style={{ fontWeight: 'bold' }}>Page: {page}</p>
            </div>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <label htmlFor="pageSize" style={{ marginRight: '10px' }}>
                    {t("itemsPerPage")}:{' '}
                </label>
                <select id="pageSize" value={localPageSize} onChange={handlePageSizeChange}>
                    <option value={1}>1</option>
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                </select>
                <Pagination count={totalPages} page={page} onChange={handleChange} style={{ marginLeft: '10px' }} />
            </div>
        </div>
    );
};

export default CustomPagination;
