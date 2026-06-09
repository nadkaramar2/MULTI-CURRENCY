import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import { v4 as uuidv4 } from "uuid";
import AutoPagintation from "../../UI/AutoPagintation";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function ViewWalletAccountMaster() {
  const [accounttype, setAccountType] = useState("");
  const [data, setData] = useState([]);
  const [multicurrency, setMulticurrency] = useState([]);
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [itemPage, setItemPage] = useState(20);
  const [tcount, setTcount] = useState(1);
  const [totalElement, setTotalElement] = useState("");


  const [checkpoint, setCheckpoint] = useState(0);

  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);
  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  useEffect(() => {
    accountytpe();
  }, []);

  const accountytpe = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getaccounttypelistbasedonmulticurrency`,
        {}
      );

      if (response.data.code === "S0000") {
        setData(response.data.accountDescription);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  useEffect(() => {
    View();
  }, [accounttype, tcount, itemPage]);

  const View = async () => {
    if (accounttype === "" || accounttype.length === 0) {
      // handleShowError("Please enter the Account type");
    } else {
      try {
        const response = await amsApi.post(
          `multiCurrencyWallletAccounttype/viewMultiCurrencyWalletAccountMaster`,
          {
            baseCurrencyAccountType: accounttype,
            pageSize: itemPage,
            pageNumber: tcount,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (response.data.code === "S0000") {
          setTotalElement(response.data.totalElementsSize);
          setMulticurrency(response.data.multiCurrencyAccountData);
          setCheckpoint(1);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  
   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       View();
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `accountType/accountTypeSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setMulticurrency(response.data.acctTypeSearch);
         // handleShowSuccess(response.data.message);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Multicurrency AccountType Linked GL
              </p>

              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-8  focus:outline-none rounded text-xs font-normal">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>

                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",

                      "--Switch-trackWidth": "64px",

                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>

        {checked && (
          <div className=" md:col-span-2 lg:col-span-1  ">
            <form className="">
              <div className="overflow-hidden shadow ">
                <div className=" px-2 sm:p-2 bg-white">
                  <div className="grid gap-6  md:grid-cols-3 px-8">
                    <div>
                      <label
                        htmlFor="Account Type"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Account Type{""}
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="accounttype"
                        name="accounttype"
                        value={accounttype}
                        onChange={(e) => setAccountType(e.target.value)}
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        <option value="All">ALL</option>
                        {data.map((item) => (
                          <option value={item.strAccountType}>
                            {item.strAccountType || ""}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        )}
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={totalElement}
          reCallApi={View}
        /> */}

        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={multicurrency[0]?.strTotalCount}
          reCallApi={Search}
        />
        <div className="overflow-x-auto relative shadow-md  ">
          <>
            <div className="table-wrp block max-h-[33rem] ">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap ">
                    currency Code
                  </th>
                  <th scope="col" className="py-2.5 px-6  whitespace-nowrap ">
                    priority
                  </th>
                  <th scope="col" className="py-2.5 px-6  whitespace-nowrap ">
                    gl Account Type
                  </th>
                  <th scope="col" className="py-2.5 px-6  whitespace-nowrap ">
                    gl Account Number
                  </th>
                </thead>

                <tbody>
                  {multicurrency.length > 0 ? (
                    <>
                      {multicurrency
                        
                        .map(
                          ({
                            currencyCode,
                            priority,
                            glAccountType,
                            glAccountNumber,
                          }) => (
                            <tr
                              key={uuidv4()}
                              className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                            >
                              <td className="px-6 py-2.5 whitespace-nowrap">
                                <div className="text-smclassName text-gray-900">
                                  {currencyCode || "-"}
                                </div>
                              </td>
                              <td className="px-6 py-2.5 whitespace-nowrap">
                                <div className="text-smclassName text-gray-900">
                                  {priority || "-"}
                                </div>
                              </td>
                              <td className="px-6 py-2.5 whitespace-nowrap">
                                <div className="text-smclassName text-gray-900">
                                  {glAccountType || "-"}
                                </div>
                              </td>
                              <td className="px-6 py-2.5 whitespace-nowrap">
                                <div className="text-smclassName text-gray-900">
                                  {glAccountNumber || "-"}
                                </div>
                              </td>
                            </tr>
                          )
                        )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="12" className="px-6 py-2 text-center">
                        <span className="text-base font-medium text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
